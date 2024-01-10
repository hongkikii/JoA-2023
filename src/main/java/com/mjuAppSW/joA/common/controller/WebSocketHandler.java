package com.mjuAppSW.joA.common.controller;

import static com.mjuAppSW.joA.common.constant.Constants.*;
import static com.mjuAppSW.joA.common.constant.Constants.WebSocketHandler.*;

import com.mjuAppSW.joA.common.auth.MemberChecker;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.message.MessageService;
import com.mjuAppSW.joA.domain.report.message.MessageReport;
import com.mjuAppSW.joA.domain.report.message.MessageReportRepository;
import com.mjuAppSW.joA.domain.room.Room;
import com.mjuAppSW.joA.domain.room.RoomRepository;
import com.mjuAppSW.joA.domain.room.RoomService;
import com.mjuAppSW.joA.domain.room.exception.RoomNotFoundException;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMember;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberService;
import com.mjuAppSW.joA.domain.roomInMember.dto.RoomListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private Map<String, List<WebSocketSession>> memberSessions = new ConcurrentHashMap<>();
    private final RoomInMemberService roomInMemberService;
    private final RoomInMemberRepository roomInMemberRepository;
    private final MessageService messageService;
    private final RoomRepository roomRepository;
    private final MemberChecker memberChecker;
    private final MemberRepository memberRepository;
    private final RoomService roomService;
    private final MessageReportRepository messageReportRepository;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        String[] arr = payload.split(SEPARATOR, LIMIT_SEPARATOR);

        String separator = arr[0];
        if(separator.equals(R_SEPARATOR)){
            makeRoom(arr[1], sessionIdToMemberId(arr[2]), arr[3]);
        }else if(separator.equals(M_SEPARATOR)){
            sendMessage(arr[1], sessionIdToMemberId(arr[2]), arr[3], session);
        }
    }

    public String sessionIdToMemberId(String token){
        // sessionId가 없을 때 Exception 처리
        Member member = memberRepository.findBysessionId(Long.parseLong(token)).orElse(null);
        if(member != null){
            String memberId = String.valueOf(member.getId());
            return memberId;
        }
        return null;
    }

    // public void makeRoom(String roomId, String memberId1, String memberId2){
    //     String memberId = sessionIdToMemberId(memberId1);
    //
    //     Boolean checkRoomId = roomInMemberService.findByRoom(Long.parseLong(roomId));
    //
    //     if(checkRoomId){
    //         Boolean room1 = roomInMemberService.createRoom(Long.parseLong(roomId), Long.parseLong(memberId));
    //         Boolean room2 = roomInMemberService.createRoom(Long.parseLong(roomId), Long.parseLong(memberId2));
    //         if(room1 && room2) {log.info("makeRoom : roomId = {}, memberId1 = {}, memberId2 = {}", roomId, memberId, memberId2);}
    //         else{log.warn("makeRoom : getValue's not correct or already exist / roomId = {}, memberId1 = {}, memberId2 = {}", roomId, memberId1, memberId2);}
    //     }else log.warn("makeRoom : getValue's not correct or already exist / roomId = {}", roomId);
    // }

    public void makeRoom(String roomId, String memberId1, String memberId2){
        Boolean checkRoomInMember = roomInMemberService.findByRoom(Long.parseLong(roomId));
        if (checkRoomInMember){
            roomInMemberService.createRoom(Long.parseLong(roomId), Long.parseLong(memberId1));
            roomInMemberService.createRoom(Long.parseLong(roomId), Long.parseLong(memberId2));
        }
    }


    // public void sendMessage(String roomId, String memberId1, String content,
    //                         WebSocketSession session) throws Exception {
    //     String memberId = sessionIdToMemberId(memberId1);
    //     // check MessageReport
    //     List<MessageReport> checkMessageReport = messageReportRepository.findByRoomId(Long.parseLong(roomId));
    //     // check Expired
    //     Boolean checkExpired = roomInMemberService.checkExpired(Long.parseLong(roomId), Long.parseLong(memberId));
    //     // check createdAt
    //     Integer checkTime = roomService.checkTime(Long.parseLong(roomId));
    //     // check isWithDrawal
    //     Boolean checkIsWithDrawal = roomInMemberService.checkIsWithDrawal(Long.parseLong(roomId), Long.parseLong(memberId));
    //
    //     if(checkExpired && checkTime == 0 && checkIsWithDrawal && checkMessageReport.isEmpty()){
    //         List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
    //         if(roomSessionsList != null){
    //             int count = 2;
    //             for(int i=0; i<roomSessionsList.size(); i++){
    //                 count--;
    //             }
    //             String isChecked = String.valueOf(count);
    //             Long saveId = messageService.saveMessage(Long.parseLong(roomId), Long.parseLong(memberId), content, isChecked);
    //             if(saveId != null){
    //                 Boolean update = updateCurrentMessage(roomId, memberId);
    //                 if(update){log.info("updateRoomList : roomId = {}, memberId = {}", roomId, memberId);}
    //                 log.info("SaveMessage : roomId = {}, memberId = {}, content = {}, isChecked = {}", roomId, memberId, content, isChecked);
    //                 for (WebSocketSession targetSession : roomSessionsList) {
    //                     if (targetSession.isOpen() && !targetSession.equals(session)) {
    //                         log.info("sendMessage : roomId = {}, memberId = {}, content = {}", roomId, memberId, content);
    //                         targetSession.sendMessage(new TextMessage(saveId + " " + content));
    //                     }
    //                     if (targetSession.isOpen() && targetSession.equals(session) && roomSessionsList.size() == 2){
    //                         log.info("sendMessage : send 0 sessionURI = {}", session.getUri());
    //                         targetSession.sendMessage(new TextMessage("0"));
    //                     }
    //                 }
    //             }else{
    //                 log.warn("SaveMessage : getValue's not correct / roomId = {}, memberId = {}", roomId, memberId);
    //             }
    //         }
    //     }else if(!checkExpired){
    //         log.info("checkExpired '0' : roomId = {}", roomId);
    //         String exitMessage = "상대방이 채팅방을 나갔습니다.";
    //         sendExceptionMessage(roomId, session, exitMessage);
    //     }else if(checkTime != 0){
    //         String alarmMessage = "방 유효시간이 지났기 때문에 메시지를 보낼 수 없습니다.";
    //         if(checkTime == 1){
    //             log.info("checkTime over 24hours : roomId = {}", roomId);
    //         }else if(checkTime == 7){
    //             log.info("checkTime over 7days : roomId = {}", roomId);
    //         }else{
    //             log.warn("checkTime : getValue's not correct / roomId = {}", roomId);
    //             alarmMessage = "방 정보가 유효하지 않습니다.";
    //         }
    //         sendExceptionMessage(roomId, session, alarmMessage);
    //     }else if(!checkIsWithDrawal){
    //         log.info("checkIsWithDrawal : roomId = {}", roomId);
    //         String outMessage = "상대방이 탈퇴하였습니다.";
    //         sendExceptionMessage(roomId, session, outMessage);
    //     }else if(!checkMessageReport.isEmpty()){
    //         log.info("checkMessageReport : roomId = {}", roomId);
    //         String outMessage = "신고된 방입니다.";
    //         sendExceptionMessage(roomId, session, outMessage);
    //     }
    // }
    public void sendMessage(String roomId, String memberId, String content, WebSocketSession session) throws Exception {
        if(isMessageSendable(Long.parseLong(roomId), Long.parseLong(memberId), session)){
            List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
            if (roomSessionsList != null) {
                int count = roomSessionsList.size();
                for(int i=0; i<roomSessionsList.size(); i++) count--;
                String isChecked = String.valueOf(count);
                Long saveId = saveAndSendMessage(roomId, memberId, content, isChecked);
                if (saveId != null) {
                    updateAndNotify(roomId, memberId, content, isChecked, saveId, session, roomSessionsList);
                } else {
                    log.warn("SaveMessage : getValue's not correct / roomId = {}, memberId = {}", roomId, memberId);
                }
            }
        }
    }

    private boolean isMessageSendable(Long roomId, Long memberId, WebSocketSession session) throws IOException {
        // find Room
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        // check MessageReport
        List<MessageReport> checkMessageReport = messageReportRepository.findByRoomId(room);
        // check Expired
        Boolean checkExpired = roomInMemberService.checkExpired(roomId, memberId);
        // check isWithDrawal
        Boolean checkIsWithDrawal = roomInMemberService.checkIsWithDrawal(roomId, memberId);
        // check createdAt
        Integer checkTime = roomService.checkTime(roomId);

        if(!checkMessageReport.isEmpty()){
            sendExceptionMessage(String.valueOf(roomId), session, "신고된 방입니다.");
            return false;
        }else if(!checkExpired){
            sendExceptionMessage(String.valueOf(roomId), session, "상대방이 나갔습니다.");
            return false;
        }else if(!checkIsWithDrawal){
            sendExceptionMessage(String.valueOf(roomId), session, "상대방이 탈퇴하였습니다.");
            return false;
        }else if(checkTime != NORMAL_OPERATION){
            handleTimeRelatedIssues(roomId, checkTime, session);
            return false;
        }
        return true;
    }

    private void handleTimeRelatedIssues(Long roomId, Integer checkTime, WebSocketSession session) throws IOException {
        String alarmMessage = "";
        if (checkTime == OVER_ONE_DAY) {
            alarmMessage = "방 유효시간이 24시간을 초과하였습니다.";
        } else if (checkTime == OVER_SEVEN_DAY) {
            alarmMessage = "방 유효시간이 7일을 초과하였습니다.";
        }
        sendExceptionMessage(String.valueOf(roomId), session, alarmMessage);
    }

    private void sendExceptionMessage(String roomId, WebSocketSession session, String message) throws IOException {
        List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
        if (roomSessionsList != null) {
            for (WebSocketSession targetSession : roomSessionsList) {
                if (targetSession.equals(session)) {
                    targetSession.sendMessage(new TextMessage(message));
                }
            }
        }
    }

    private Long saveAndSendMessage(String roomId, String memberId, String content, String isChecked) {
        LocalDateTime createdMessageDate = LocalDateTime.now();
        Long saveId = messageService.saveMessage(Long.parseLong(roomId), Long.parseLong(memberId), content, isChecked, createdMessageDate);
        if (saveId != null) return saveId;
        return null;
    }

    private void updateAndNotify(String roomId, String memberId, String content, String isChecked, Long saveId,
        WebSocketSession session, List<WebSocketSession> roomSessionsList) throws Exception {
        Boolean update = updateCurrentMessage(roomId, memberId);
        if (update) {log.info("updateRoomList : roomId = {}, memberId = {}", roomId, memberId);}
        log.info("SaveMessage : roomId = {}, memberId = {}, content = {}, isChecked = {}", roomId, memberId, content, isChecked);
        for (WebSocketSession targetSession : roomSessionsList) {
            if (targetSession.isOpen() && !targetSession.equals(session)) {
                log.info("sendMessage : roomId = {}, memberId = {}, content = {}", roomId, memberId, content);
                targetSession.sendMessage(new TextMessage(saveId + " " + content));
            }
            if (targetSession.isOpen() && targetSession.equals(session) && roomSessionsList.size() == 2) {
                log.info("sendMessage : send 0 sessionURI = {}", session.getUri());
                targetSession.sendMessage(new TextMessage(OPPONENT_CHECK_MESSAGE));
            }
        }
    }
    // 리팩토링 어떻게 할지.. websocket을 사용할때 프론트에게 다시 보내는 방법은 Text 기반인데, Exception을 어떻게 전달할 수 있는거지?
    // 만약 RunTime에서 Exception이 생겼다면 이걸 Log로 남길 수 있는 방법을 찾아야함. RuntimeError이면 무중단이 가능한가?
    public Boolean updateCurrentMessage(String roomId, String memberId) throws Exception {
        Room room = roomRepository.findById(Long.parseLong(roomId)).orElseThrow(RoomNotFoundException::new);
        Member member = memberChecker.findById(Long.parseLong(memberId));
        if(room != null && member != null){
            RoomInMember roomInMember = roomInMemberRepository.checkOpponentExpired(room, member);
            List<WebSocketSession> memberSessionsList = memberSessions.get(String.valueOf(roomInMember.getMember().getId()));
            if(memberSessionsList == null || memberSessionsList.isEmpty()){
                return false;
            }
            for(WebSocketSession targetSession : memberSessionsList){
                if(targetSession.isOpen()){
                    RoomListDTO roomDTO = roomInMemberService.getUpdateRoom(room, member);
                    String message = roomDTO.getRoomId() + " " + roomDTO.getName() + " " +
                            roomDTO.getUrlCode() + " " + roomDTO.getUnCheckedMessage() + " " + roomDTO.getContent();
                    log.info("updateCurrentMessage : message = {}", message);
                    targetSession.sendMessage(new TextMessage(message));
                    return true;
                }
            }
            log.info("updateCurrentMessage Fail : roomId = {}, memberId = {}", roomInMember.getRoom().getId(), roomInMember.getMember().getId());
        }
        return false;
    }

    private String getRoomId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        String[] arr = query.split("&");
        String getString = arr[arr.length-2];
        String[] getArr = getString.split("=");
        String getRoomId = getArr[getArr.length-1];
        return getRoomId;
    }

    private String getMemberId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        String[] arr = query.split("&");
        String getString = arr[arr.length-1];
        String[] getArr = getString.split("=");
        String getMemberId = getArr[getArr.length-1];
        return getMemberId;
    }

    private String getOnlyMemberId(WebSocketSession session) throws URISyntaxException{
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        String[] getArr = query.split("=");
        String getMemberId = getArr[getArr.length-1];
        return getMemberId;
    }

    public Integer parseUri (WebSocketSession session) throws URISyntaxException{
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        if(query == null){
            return 0;
        }else{
            String[] arr = query.split("&");
            if(arr.length == 1){
                return 1;
            }
            return 2;
        }
    }

    public Boolean checkURI(WebSocketSession session, String roomId){
        List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
        if(roomSessionsList == null || roomSessionsList.isEmpty()){
            return true;
        }else{
            for(WebSocketSession ss : roomSessionsList){
                if(ss.getUri().toString().equals(session.getUri().toString())){
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean checkURIOfMemberId(WebSocketSession session, String memberId){
        List<WebSocketSession> memberRoomList = memberSessions.get(memberId);
        if(memberRoomList == null || memberRoomList.isEmpty()){
            return true;
        }else{
            for(WebSocketSession ss : memberRoomList){
                if(ss.getUri().toString().equals(session.getUri().toString())){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        Integer length = parseUri(session);
        switch (length){
            case 1 :
                Long onlyMemberId = Long.parseLong(getOnlyMemberId(session));
                Member member = memberRepository.findBysessionId(onlyMemberId).orElse(null);
                if(member != null){
                    String memberId = String.valueOf(member.getId());
                    if(checkURIOfMemberId(session, memberId)){
                        log.info("websocketConnect : memberId = {}", memberId);
                        memberSessions.computeIfAbsent(memberId, key -> new ArrayList<>()).add(session);
                    }else{
                        log.info("Already existed websocket : memberId = {}", memberId);
                    }
                }else{
                    log.warn("websocketConnect Fail : memberId = {}", onlyMemberId);
                }
                break;
            case 2 :
                String roomId = getRoomId(session);
                Long memberOfSessionId = Long.parseLong(getMemberId(session));
                Member mem = memberRepository.findBysessionId(memberOfSessionId).orElse(null);
                if(mem != null) {
                    String memberId = String.valueOf(mem.getId());
                    Boolean checkURI = checkURI(session, roomId);
                    if (checkURI) {
                        log.info("websocketConnect : roomId = {}, memberId = {}", roomId, memberId);
                        roomSessions.computeIfAbsent(roomId, key -> new ArrayList<>()).add(session);
                        Boolean updateEntryTime = roomInMemberService.updateEntryTime(roomId, memberId);
                        if (updateEntryTime) {
                            log.info("updateEntryTime : roomId = {}, memberId = {}", roomId, memberId);
                        } else {
                            log.warn("updateEntryTime : getValue's not correct / roomId = {}, memberId = {}", roomId, memberId);
                        }

                        Boolean updateIsChecked = messageService.updateIsChecked(roomId, memberId);
                        if (updateIsChecked) {
                            log.info("updateIsChecked : roomId = {}, memberId = {}", roomId, memberId);
                        } else {
                            log.warn("updateIsChecked : getValue's not correct / roomId = {}, memberId = {}", roomId, memberId);
                        }

                        List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
                        Integer checkTime = roomService.checkTime(Long.parseLong(roomId));
                        for (WebSocketSession targetSession : roomSessionsList) {
                            if (targetSession.isOpen() && !targetSession.equals(session) && checkTime == 0) {
                                String check = "0";
                                log.info("Opponent has entered, targetSession = {}, check = {}", targetSession, check);
                                targetSession.sendMessage(new TextMessage(check));
                            }
                        }
                    }else{
                        log.warn("already existed websocket : roomId = {}, memberId = {}", roomId, memberId);
                    }
                }else{
                    log.warn("websocketConnect Fail : roomId = {}, sessionId = {}", roomId, memberOfSessionId);
                }
                break;
            default:
                log.info("websocketConnect : /ws");
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer length = parseUri(session);
        switch (length){
            case 1 :
                Long onlyMemberId = Long.parseLong(getOnlyMemberId(session));
                Member member = memberRepository.findBysessionId(onlyMemberId).orElse(null);
                if(member != null){
                    String memberId = String.valueOf(member.getId());
                    log.info("websocketClosed : memberId = {}", memberId);
                    List<WebSocketSession> memberSessionsList = memberSessions.get(memberId);
                    if(memberSessionsList != null){
                        memberSessionsList.remove(session);
                        if(memberSessionsList.isEmpty()){
                            log.info("websocketClosed Remove : memberId = {}", memberId);
                            memberSessions.remove(memberId);
                        }
                    }
                }
                break;
            case 2 :
                String roomId = getRoomId(session);
                Long memberOfSessionId = Long.parseLong(getMemberId(session));
                Member mem = memberRepository.findBysessionId(memberOfSessionId).orElse(null);
                if(mem != null) {
                    String memberId = String.valueOf(mem.getId());
                    log.info("websocketClosed : roomId = {}, memberId = {}", roomId, memberId);
                    List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
                    if (roomSessionsList != null) {
                        roomSessionsList.remove(session);
                        if (roomSessionsList.isEmpty()) {
                            log.info("websocketClosed Remove : roomId = {}, memberId = {}", roomId, memberId);
                            roomSessions.remove(roomId);
                        }
                    }else{
                        log.warn("websocketClosed can't find : roomId = {}, memberId = {}", roomId, memberId);
                    }
                    Boolean updateExitTime = roomInMemberService.updateExitTime(roomId, memberId);
                    if (updateExitTime) {
                        log.info("updateExitTime : roomId = {}, memberId = {}", roomId, memberId);
                    } else {
                        log.warn("updateExitTime : getValue's not correct / roomId = {}, memberId = {}", roomId, memberId);
                    }
                }else{
                    log.warn("websocketClosed can't find memberId : roomId = {}, session = {}", roomId, memberOfSessionId);
                }
                break;
            default:
                log.info("websocketClosed : /ws");
                break;
        }
    }
}
