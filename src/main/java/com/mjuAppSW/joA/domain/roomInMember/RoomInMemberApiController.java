package com.mjuAppSW.joA.domain.roomInMember;

import com.mjuAppSW.joA.common.dto.SuccessResponse;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.CheckRoomInMemberRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.UpdateExpiredRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.request.VoteRequest;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.RoomListResponse;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.UserInfoResponse;
import com.mjuAppSW.joA.domain.roomInMember.dto.response.VoteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/joa/room-in-members")
public class RoomInMemberApiController {
    private final RoomInMemberService roomInMemberService;

    // @GetMapping("/load/roomList")
    // public ResponseEntity<List<RoomListDTO>> getRoomList(
    //         @RequestParam("memberId") Long memberId) {
    //     log.info("getRoomList : memberId = {}", memberId);
    //     RoomListVO response = roomInMemberService.getRoomList(memberId);
    //     if(response.getStatus().equals("0") || response.getStatus().equals("1")){
    //         log.info("getRoomList Return : OK");
    //         return ResponseEntity.ok(response.getRoomDTOList());
    //     }else if(response.getStatus().equals("3")){
    //         log.warn("getRoomList Return : BAD_REQUEST, member id is not valid / memberId = {}", memberId);
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    //     }
    //     else{
    //         log.warn("getRoomList Return : BAD_REQUEST, roomId's not correct / memberId = {}", memberId);
    //         return ResponseEntity.badRequest().build();
    //     }
    // }
    @Operation(summary = "채팅방 목록 페이지 정보 조회", description = "채팅방 목록 페이지에서 채팅 목록 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "채팅방 목록 정보 반환"),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "", description = "사용 정지 회원"),
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<SuccessResponse<RoomListResponse>> getRoomList(@PathVariable("memberId") Long memberId){
        return SuccessResponse.of(roomInMemberService.getRoomList(memberId))
            .asHttp(HttpStatus.OK);
    }

    // @PostMapping("/save/voteResult") // 투표 저장하기
    // public ResponseEntity<VoteDTO> saveVoteResult(@RequestBody VoteRequest request){
    //     log.info("saveVoteResult : roomId = {}, memberId = {}, result = {}", request.getRoomId(), request.getMemberId(), request.getResult());
    //     Boolean checkRoomIdAndMemberId = roomInMemberService.checkRoomIdAndMemberId(request.getRoomId(), request.getMemberId());
    //     if(!checkRoomIdAndMemberId){
    //         log.warn("saveVoteResult Return : BAD_REQUEST, getValue's not correct / roomId = {}, memberId = {}", request.getRoomId(), request.getMemberId());
    //         return ResponseEntity.badRequest().build();
    //     }
    //     VoteDTO voteDTO = roomInMemberService.saveVoteResult(request.getRoomId(), request.getMemberId(), request.getResult());
    //     if(voteDTO != null){
    //         log.info("saveVoteResult Return : roomId = {}, memberId = {}, result = {}", voteDTO.getRoomId(), voteDTO.getMemberId(), voteDTO.getResult());
    //         return ResponseEntity.ok(voteDTO);
    //     }
    //     log.warn("saveVoteResult Return : NOT_FOUND, fail to save / roomId = {}, memberId = {}, result = {}", request.getRoomId(), request.getMemberId(), request.getResult());
    //     return ResponseEntity.notFound().build();
    // }
    @Operation(summary = "채팅방 연장 투표 저장 및 상대방 투표 유무 확인", description = "채팅방 연장 투표 저장 및 상대방 투표 유무 확인 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "채팅방 연장 투표 저장 및 상대방 투표 유무 반환"),
        @ApiResponse(responseCode = "404", description = "R001: 방을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "RIM001: 채팅방을 찾을 수 없습니다.")
    })
    @PostMapping("/result")
    public ResponseEntity<SuccessResponse<VoteResponse>> saveVoteResult(@RequestBody @Valid VoteRequest request){
        return SuccessResponse.of(roomInMemberService.saveVoteResult(request))
            .asHttp(HttpStatus.OK);
    }

    // 불필요 AND 삭제 API
    // 채팅방 연장 투표 후 상대방의 투표 유무를 반환해주기 때문에 만약 찬성이라면 바로 연장 API를 요청하면 됌.
    // @GetMapping("/check/voteResult")
    // public ResponseEntity<List<CheckVoteDTO>> checkVoteResult(
    //         @RequestParam("roomId") Long roomId){
    //     log.info("checkVoteResult : roomId = {}", roomId);
    //     Boolean checkRoomIdAndMemberId = roomInMemberService.checkRoomId(roomId);
    //     if(!checkRoomIdAndMemberId){
    //         log.warn("checkVoteResult Return : BAD_REQUEST, roomId's not found / roomId = {}", roomId);
    //         return ResponseEntity.badRequest().build();
    //     }
    //     List<CheckVoteDTO> list = roomInMemberService.checkVoteResult(roomId);
    //     if(list.isEmpty() || list == null){
    //         log.warn("checkVoteResult Return : BAD_REQUEST, In roomInMember's not found / roomId = {}", roomId);
    //         return ResponseEntity.badRequest().build();
    //     }
    //     for(CheckVoteDTO checkVoteDTO : list){
    //         log.info("checkVoteResult Return : OK, roomId = {}, memberId = {}, result = {}", checkVoteDTO.getRoomId(), checkVoteDTO.getMemberId(), checkVoteDTO.getResult());
    //     }
    //     return ResponseEntity.ok(list);
    // }

    // @PostMapping("/update/expired")
    // public HttpStatus updateExpired(@RequestBody updateExpiredRequest request){
    //     log.info("updateExpired : roomId = {}, memberId = {}, expired = {}", request.getRoomId(), request.getMemberId(), request.getExpired());
    //     Boolean save = roomInMemberService.updateExpired(request.getRoomId(), request.getMemberId(), request.getExpired());
    //     if(!save){
    //         log.warn("updateExpired Return : BAD_REQUEST, getValue's not found / roomId = {}, memberId = {}, expired = {}", request.getRoomId(), request.getMemberId(), request.getExpired());
    //         return HttpStatus.BAD_REQUEST;
    //     }
    //     log.info("updateExpired Return : OK");
    //     return HttpStatus.OK;
    // }

    @Operation(summary = "채팅방 퇴장", description = "채팅방 퇴장 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태코드 반환"),
        @ApiResponse(responseCode = "404", description = "R001: 방을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "RIM001: 채팅방을 찾을 수 없습니다.")
    })
    @PatchMapping("/expired")
    public ResponseEntity<Void> updateExpired(@RequestBody @Valid UpdateExpiredRequest request){
        roomInMemberService.updateExpired(request);
        return ResponseEntity.ok().build();
    }

    // @PostMapping("/check/roomInMember")
    // public ResponseEntity<String> checkRoomInMember(@RequestBody CheckRoomInMemberRequest request){
    //     log.info("checkRoomInMember : memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //     Boolean check = roomInMemberService.checkRoomInMember(request.getMemberId1(), request.getMemberId2());
    //     if(check){
    //         log.info("checkRoomInMember Return : OK");
    //         return new ResponseEntity(HttpStatus.OK);
    //     }else{
    //         log.warn("checkRoomInMember Return : BAD_REQUEST, getValue's not found / memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //         return new ResponseEntity(HttpStatus.BAD_REQUEST);
    //     }
    // }

    @Operation(summary = "채팅방 생성 전 채팅방 유무 확인", description = "채팅 방 생성 전 이미 두 사용자의 채팅방이 존재하는지 확인 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태코드 반환"),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "400", description = "RIM002: 이미 두 사용자의 채팅방이 존재합니다.")
    })
    @PostMapping("/check")
    public ResponseEntity<Void> checkRoomInMember(@RequestBody CheckRoomInMemberRequest request){
        roomInMemberService.checkRoomInMember(request);
        return ResponseEntity.ok().build();
    }

    // @GetMapping("/load/userInfo")
    // public ResponseEntity<UserInfoDTO> getUserInfo(@RequestParam("roomId") Long roomId,
    //                                                @RequestParam("memberId") Long memberId){
    //     log.info("getUserInfo : roomId = {}, memberId = {}", roomId, memberId);
    //     UserInfoDTO userInfoDTO = roomInMemberService.getUserInfo(roomId, memberId);
    //     if(userInfoDTO.getName() == null){
    //         log.warn("getUserInfo Return : BAD_REQUEST, name is null / roomId = {}, memberId = {}", roomId, memberId);
    //         return ResponseEntity.badRequest().body(userInfoDTO);
    //     }
    //     log.info("userInfoDTO : name = {}, urlCode = {}, bio = {}", userInfoDTO.getName(),
    //             userInfoDTO.getUrlCode(), userInfoDTO.getBio());
    //     return ResponseEntity.ok(userInfoDTO);
    // }
    @Operation(summary = "채팅방 입장시 상대방 정보 조회", description = "상대방 정보 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상대방 정보 반환"),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "R001: 방을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "RIM001: 채팅방을 찾을 수 없습니다.")
    })
    @GetMapping("/{roomId}/{memberId}/userInfo")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserInfo(@PathVariable("roomId") Long roomId, @PathVariable("memberId") Long memberId){
        return SuccessResponse.of(roomInMemberService.getUserInfo(roomId, memberId))
            .asHttp(HttpStatus.OK);
    }
}