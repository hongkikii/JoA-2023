package com.mjuAppSW.joA.domain.roomInMember;

import static com.mjuAppSW.joA.common.constant.Constants.RoomInMember.*;

import com.mjuAppSW.joA.common.auth.MemberChecker;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.message.dto.vo.CurrentMessageVO;
import com.mjuAppSW.joA.domain.room.Room;
import com.mjuAppSW.joA.domain.room.RoomRepository;
import com.mjuAppSW.joA.domain.room.RoomService;
import com.mjuAppSW.joA.domain.room.exception.RoomNotFoundException;
import com.mjuAppSW.joA.domain.roomInMember.dto.*;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.CheckRoomInMemberRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.UpdateExpiredRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.VoteRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.RoomListResponse;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.UserInfoResponse;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.VoteResponse;
import com.mjuAppSW.joA.domain.roomInMember.vo.RoomInfoExceptMessageVO;
import com.mjuAppSW.joA.domain.roomInMember.vo.RoomInfoIncludeMessageVO;
import com.mjuAppSW.joA.domain.roomInMember.vo.RoomInfoVO;
import com.mjuAppSW.joA.domain.roomInMember.vo.UserInfoVO;
import com.mjuAppSW.joA.domain.roomInMember.exception.AlreadyExistedRoomInMemberException;
import com.mjuAppSW.joA.domain.roomInMember.exception.RoomInMemberNotFoundException;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomInMemberService {
    private static String alg = "AES/CBC/PKCS5Padding";
    private final RoomInMemberRepository roomInMemberRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final MemberChecker memberChecker;

    public String decrypt(String cipherText, String encryptionKey){
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            IvParameterSpec IV = new IvParameterSpec(encryptionKey.substring(0,16).getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, IV);
            byte[] decodeByte = Base64.getDecoder().decode(cipherText);
            return new String(cipher.doFinal(decodeByte), "UTF-8");
        }catch(Exception e){
            log.warn("RoomInMemberService, decrypt is Error");
        }
        return null;
    }

    // public Boolean findByRoomIdAndMemberId(Long roomId, Long memberId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findById(memberId).orElse(null);
    //     if(room != null && member != null){
    //         RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member);
    //         if(roomInMember != null){
    //             String status = roomInMember.getExpired();
    //             if(status == "0") return true;
    //         }return true;
    //     }return false;
    // }

    // public Boolean findByRoom(Long roomId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     if(room != null){
    //         List<RoomInMember> roomInMemberList = roomInMemberRepository.findAllRoom(room);
    //         if(roomInMemberList.isEmpty() || roomInMemberList == null){
    //             return true;
    //         }
    //         for(RoomInMember roomInMember : roomInMemberList){
    //             if(roomInMember.getExpired().equals("0")){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
    public Boolean findByRoom(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        List<RoomInMember> roomInMemberList = roomInMemberRepository.findAllRoom(room);
        if(roomInMemberList.isEmpty() || roomInMemberList == null){
            return true;
        }
        for(RoomInMember roomInMember : roomInMemberList) {
            if (roomInMember.getExpired().equals(EXIT)) {
                return true;
            }
        }

        return false;
    }

    // public RoomListVO getRoomList(Long memberId) {
    //     Member member = memberRepository.findBysessionId(memberId).orElse(null);
    //     if(member != null){
    //         if(memberAccessor.isStopped(member.getSessionId())){return new RoomListVO(null, "3");}
    //         List<RoomInMember> memberList = roomInMemberRepository.findByAllMember(member);
    //         List<RoomInfoVO> roomInfoCIsNullList = new ArrayList<>();
    //         List<RoomInfoVO> roomInfoCIsNotNullList = new ArrayList<>();
    //         List<RoomListDTO> roomListDTOList = new ArrayList<>();
    //         if(memberList.isEmpty() || member == null){
    //             return new RoomListVO(null, "1");
    //         }
    //         for(RoomInMember rim : memberList){
    //             List<RoomInMember> list = roomInMemberRepository.findByAllRoom(rim.getRoom());
    //             for(RoomInMember rim1 : list){
    //                 if(rim1.getMember() != member){
    //                     RoomInfo roomInfo = roomInMemberRepository.findRoomInfoValue(rim1.getMember(), rim1.getRoom());
    //                     CurrentMessageInfo currentMessageInfo = messageRepository.getCurrentMessageAndTime(rim1.getRoom());
    //                     Integer unCheckedMessage = messageRepository.countUnCheckedMessage(rim1.getRoom(), rim1.getMember());
    //                     if(roomInfo != null){
    //                         RoomInfoVO roomInfoVO;
    //                         if(currentMessageInfo == null) {
    //                             roomInfoVO = new RoomInfoVO(roomInfo.getRoom().getId(), roomInfo.getName(), roomInfo.getUrlCode(),
    //                                     null, roomInfo.getDate(), String.valueOf(unCheckedMessage));
    //                             roomInfoCIsNullList.add(roomInfoVO);
    //                         }else {
    //                             String decryptedString = decrypt(currentMessageInfo.getContent(), rim1.getRoom().getEncryptKey());
    //                             roomInfoVO = new RoomInfoVO(roomInfo.getRoom().getId(), roomInfo.getName(), roomInfo.getUrlCode(),
    //                                     decryptedString, currentMessageInfo.getTime(), String.valueOf(unCheckedMessage));
    //                             roomInfoCIsNotNullList.add(roomInfoVO);
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //
    //         List<RoomInfoVO> combinedList = new ArrayList<>();
    //         combinedList.addAll(roomInfoCIsNotNullList);
    //         combinedList.addAll(roomInfoCIsNullList);
    //
    //            Collections.sort(combinedList, new Comparator<RoomInfoVO>() {
    //             @Override
    //             public int compare(RoomInfoVO dto1, RoomInfoVO dto2) {
    //                 return dto2.getTime().compareTo(dto1.getTime());
    //             }
    //         });
    //
    //         for(RoomInfoVO roomInfoVO : combinedList) {
    //             RoomListDTO roomDTO = new RoomListDTO(roomInfoVO.getRoomId(), roomInfoVO.getName(), roomInfoVO.getUrlCode(),
    //                     roomInfoVO.getContent(), roomInfoVO.getUnCheckedMessage());
    //             roomListDTOList.add(roomDTO);
    //         }
    //         return new RoomListVO(roomListDTOList, "0");
    //     }
    //     return new RoomListVO(null, "2");
    // }

    public RoomListResponse getRoomList(Long memberId) {
        Member member = memberChecker.findBySessionId(memberId);
        // 정지 회원에 대한 제재 처리
        if (memberChecker.isStopped(member)) {
            return null;
        }

        List<RoomInMember> memberList = roomInMemberRepository.findByAllMember(member);
        if (memberList.isEmpty()) {return RoomListResponse.of(new ArrayList<>());}

        List<RoomInfoVO> roomWithoutMessageList = new ArrayList<>();
        List<RoomInfoVO> roomWithMessageList = new ArrayList<>();
        for (RoomInMember rim : memberList) {
            // 일어날 수 없는 상황 -> 이건 어떻게 처리해야할까?
            RoomInMember anotherRoomInMember = roomInMemberRepository.findByRoomAndExceptMember(rim.getRoom(), rim.getMember())
                .orElseThrow(RoomInMemberNotFoundException::new);
            RoomInfoExceptMessageVO roomInfoEMVO = roomInMemberRepository.findRoomInfoExceptMessage(anotherRoomInMember.getRoom(), anotherRoomInMember.getMember())
                .orElseThrow(RoomInMemberNotFoundException::new);
            CurrentMessageVO currentMessageVO = messageRepository.getCurrentMessageAndTime(anotherRoomInMember.getRoom())
                .orElse(null);
            Integer unCheckedMessage = messageRepository.countUnCheckedMessage(anotherRoomInMember.getRoom(), anotherRoomInMember.getMember());
            if (roomInfoEMVO != null) {
                if (currentMessageVO == null) {
                    RoomInfoVO roomInfoVO = new RoomInfoVO(roomInfoEMVO.getRoom().getId(), roomInfoEMVO.getName(),
                        roomInfoEMVO.getUrlCode(), null, roomInfoEMVO.getDate(), String.valueOf(unCheckedMessage));
                    roomWithoutMessageList.add(roomInfoVO);
                } else {
                    String decryptedString = decrypt(currentMessageVO.getContent(), anotherRoomInMember.getRoom().getEncryptKey());
                    RoomInfoVO roomInfoVO = new RoomInfoVO(roomInfoEMVO.getRoom().getId(), roomInfoEMVO.getName(),
                        roomInfoEMVO.getUrlCode(), decryptedString, currentMessageVO.getTime(), String.valueOf(unCheckedMessage));
                    roomWithMessageList.add(roomInfoVO);
                }
            }
        }
        return RoomListResponse.of(combinedList(roomWithoutMessageList, roomWithMessageList));
    }

    private List<RoomListVO> combinedList(List<RoomInfoVO> roomWithoutMessageList, List<RoomInfoVO> roomWithMessageList){
        List<RoomListVO> roomListVOs = new ArrayList<>();
        List<RoomInfoVO> combinedList = new ArrayList<>();
        combinedList.addAll(roomWithoutMessageList);
        combinedList.addAll(roomWithMessageList);
        Collections.sort(combinedList, new Comparator<RoomInfoVO>() {
            @Override
            public int compare(RoomInfoVO dto1, RoomInfoVO dto2) {
                return dto2.getTime().compareTo(dto1.getTime());
            }
        });
        for(RoomInfoVO roomInfoVO : combinedList) {
            RoomListVO roomDTO = new RoomListVO(roomInfoVO.getRoomId(), roomInfoVO.getName(), roomInfoVO.getUrlCode(),
                roomInfoVO.getContent(), roomInfoVO.getUnCheckedMessage());
            roomListVOs.add(roomDTO);
        }
        return roomListVOs;
    }


    public RoomListDTO getUpdateRoom(Room room, Member member){
        RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);
        if(roomInMember != null){
            RoomInfoIncludeMessageVO rlr = roomInMemberRepository.findRoomInfoIncludeMessage(roomInMember.getRoom(), roomInMember.getMember())
                .orElseThrow(RoomInMemberNotFoundException::new);
            Integer unCheckedMessageCount = messageRepository.countUnCheckedMessage(roomInMember.getRoom(), roomInMember.getMember());
            if(rlr != null){
                String decryptedString = decrypt(rlr.getContent(), rlr.getRoom().getEncryptKey());
                RoomListDTO roomListDTO = new RoomListDTO(rlr.getRoom().getId(), rlr.getName(), rlr.getUrlCode(), decryptedString, String.valueOf(unCheckedMessageCount));
                return roomListDTO;
            }
        }
        return null;
    }

    // @Transactional
    // public Boolean createRoom(Long roomId, Long memberId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findById(memberId).orElse(null);
    //     if(room != null && member != null){
    //         RoomInMember roomInMember = RoomInMember.builder()
    //                 .room(room)
    //                 .member(member)
    //                 .expired("1")
    //                 .result("1")
    //                 .build();
    //         RoomInMember saveRoomInMember = roomInMemberRepository.save(roomInMember);
    //         if(saveRoomInMember != null) {return true;}
    //     }
    //     return false;
    // }
    @Transactional
    public void createRoom(Long roomId, Long memberId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findById(memberId);

        RoomInMember roomInMember = RoomInMember.builder()
            .room(room)
            .member(member)
            .expired(NOT_EXIT)
            .result(DISAPPROVE_OR_BEFORE_VOTE)
            .build();

        roomInMemberRepository.save(roomInMember);
    }

    // public VoteDTO saveVoteResult(Long roomId, Long memberId, String result){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findBysessionId(memberId).orElse(null);
    //     if(room != null && member != null){
    //         roomInMemberRepository.saveVote(room, member, result);
    //         List<RoomInMember> getRoomInMember = roomInMemberRepository.findAllRoom(room);
    //         for(RoomInMember roomInMember : getRoomInMember){
    //             if(roomInMember.getMember() != member){
    //                 VoteResponse voteResponse = new VoteResponse(roomInMember.getRoom(), roomInMember.getMember(), roomInMember.getResult());
    //                 return new VoteDTO(voteResponse.getRoom().getId(), voteResponse.getMember().getId(), voteResponse.getResult());
    //             }
    //         }
    //     }
    //     return null;
    // }

    @Transactional
    public VoteResponse saveVoteResult(VoteRequest request){
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findBySessionId(request.getMemberId());
        RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);

        roomInMember.saveResult(request.getResult());
        RoomInMember anotherRoomInMember = roomInMemberRepository.findByRoomAndExceptMember(room, member).orElse(null);
        return VoteResponse.of(anotherRoomInMember.getRoom().getId(), anotherRoomInMember.getMember().getId(), anotherRoomInMember.getResult());
    }

    // public Boolean checkRoomIdAndMemberId(Long roomId, Long memberId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findBysessionId(memberId).orElse(null);
    //     if(room != null && member != null){
    //         RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);
    //         if(roomInMember != null){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // public Boolean checkRoomInMember(Long memberId1, Long memberId2){
    //     Member member1 = memberRepository.findBysessionId(memberId1).orElse(null);
    //     Member member2 = memberRepository.findById(memberId2).orElse(null);
    //     if(member1 != null && member2 != null){
    //         List<RoomInMember> getRoomInMembers = roomInMemberRepository.checkRoomInMember(member1, member2);
    //         if(getRoomInMembers.isEmpty() || getRoomInMembers == null){
    //             return true;
    //         }
    //         for(RoomInMember rim : getRoomInMembers){
    //             if(rim.getExpired().equals("0")){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

    public void checkRoomInMember(CheckRoomInMemberRequest request){
        Member member1 = memberChecker.findBySessionId(request.getMemberId1());
        Member member2 = memberChecker.findById(request.getMemberId2());
        List<RoomInMember> getRoomInMembers = roomInMemberRepository.checkRoomInMember(member1, member2);
        for(RoomInMember rim : getRoomInMembers){
            if(rim.getExpired().equals(NOT_EXIT)){throw new AlreadyExistedRoomInMemberException();}
        }
    }

    public Boolean checkRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if(room != null){
            List<RoomInMember> roomInMemberList = roomInMemberRepository.findAllRoom(room);
            if(!roomInMemberList.isEmpty() || roomInMemberList != null){
                return true;
            }
        }
        return false;
    }

    public List<CheckVoteDTO> checkVoteResult(Long roomId){
        Room room = roomRepository.findById(roomId).orElse(null);
        if(room != null){
            List<RoomInMember> roomInMemberList = roomInMemberRepository.findAllRoom(room);
            List<CheckVoteDTO> response = new ArrayList<>();
            for(RoomInMember rim : roomInMemberList){
                CheckVoteDTO checkVoteDTO = new CheckVoteDTO(rim.getRoom().getId(), rim.getMember().getId(), rim.getResult());
                response.add(checkVoteDTO);
            }
            return response;
        }
        return new ArrayList<>();
    }

    // public UserInfoDTO getUserInfo(Long roomId, Long memberId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findBysessionId(memberId).orElse(null);
    //     if(room != null && member != null){
    //         RoomInMember getRoomInMember = roomInMemberRepository.findByRoomAndMember(room, member);
    //         if(getRoomInMember != null){
    //             UserInfo uir = roomInMemberRepository.getUserInfo(room, member);
    //             return new UserInfoDTO(uir.getName(), uir.getUrlCode(), uir.getBio());
    //         }
    //     }
    //     return new UserInfoDTO(null, null, null);
    // }
    public UserInfoResponse getUserInfo(Long roomId, Long memberId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findBySessionId(memberId);
        RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);

        UserInfoVO userInfoVO = roomInMemberRepository.getUserInfo(room, member);
        return UserInfoResponse.of(userInfoVO.getName(), userInfoVO.getUrlCode(), userInfoVO.getBio());
    }

    // @Transactional
    // public Boolean updateExpired(Long roomId, Long memberId, String expired) {
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findBysessionId(memberId).orElse(null);
    //     if(room != null && member != null){
    //         roomInMemberRepository.updateExpired(room, member, expired);
    //         return true;
    //     }
    //     return false;
    // }
    @Transactional
    public void updateExpired(UpdateExpiredRequest request) {
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findBySessionId(request.getMemberId());
        RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);

        roomInMember.updateExpired(request.getExpired());
    }

    public Boolean updateEntryTime(String sRoomId, String sMemberId){
        Room room = roomRepository.findById(Long.parseLong(sRoomId)).orElse(null);
        Member member = memberRepository.findById(Long.parseLong(sMemberId)).orElse(null);
        if(room != null && member != null){
            roomInMemberRepository.updateEntryTime(room, member, LocalDateTime.now());
            return true;
        }
        return false;
    }

    public Boolean updateExitTime(String sRoomId, String sMemberId){
        Room room = roomRepository.findById(Long.parseLong(sRoomId)).orElse(null);
        Member member = memberRepository.findById(Long.parseLong(sMemberId)).orElse(null);
        if(room != null && member != null){
            roomInMemberRepository.updateExitTime(room, member, LocalDateTime.now());
            return true;
        }
        return false;
    }

    // public Boolean checkExpired(Long roomId, Long memberId){
    //     Room room = roomRepository.findById(roomId).orElse(null);
    //     Member member = memberRepository.findById(memberId).orElse(null);
    //     if (room != null && member != null) {
    //         RoomInMember rim = roomInMemberRepository.checkExpired(room, member);
    //         if (rim.getExpired().equals("1")) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    public Boolean checkExpired(Long roomId, Long memberId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findById(memberId);
        RoomInMember roomInMember = roomInMemberRepository.findByRoomAndMember(room, member).orElseThrow(RoomInMemberNotFoundException::new);

        if(roomInMember.getExpired().equals(NOT_EXIT)){return true;}
        return false;
    }

    public Boolean checkIsWithDrawal(Long roomId, Long memberId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findById(memberId);
        RoomInMember rim = roomInMemberRepository.checkOpponentExpired(room, member);

        Member opponentMember = memberChecker.findById(rim.getMember().getId());
        if (opponentMember != null) return true;
        return false;
    }
}
