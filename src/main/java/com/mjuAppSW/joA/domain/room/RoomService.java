package com.mjuAppSW.joA.domain.room;

import static com.mjuAppSW.joA.common.constant.Constants.Room.*;

import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.report.message.MessageReport;
import com.mjuAppSW.joA.domain.report.message.MessageReportRepository;
import com.mjuAppSW.joA.domain.room.dto.response.RoomResponse;
import com.mjuAppSW.joA.domain.room.exception.OverOneDayException;
import com.mjuAppSW.joA.domain.room.exception.RoomNotFoundException;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomInMemberRepository roomInMemberRepository;
    private final MessageRepository messageRepository;
    private final MessageReportRepository messageReportRepository;

    public String makeRandomString(){
        byte[] randomBytes = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    @Transactional
    public RoomResponse createRoom(LocalDateTime createdRoomDate){
        Room room = Room.builder()
            .date(createdRoomDate)
            .status(NOT_EXTEND)
            .encryptKey(makeRandomString())
            .build();
        Room saveRoom = roomRepository.save(room);
        return RoomResponse.of(saveRoom.getId());
    }

    @Transactional
    public void updateStatusAndDate(Long roomId, LocalDateTime updateRoomStatusDate){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        room.updateStatusAndDate(updateRoomStatusDate);
    }

    public Long calculationHour(LocalDateTime getTime){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(getTime, currentDateTime);
        Long hours = duration.toHours();
        return hours;
    }

    public void checkCreateAtRoom(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        LocalDateTime getDate = room.getDate();
        Long hours = calculationHour(getDate);
        if(hours < ONE_DAY_HOURS){
            throw new OverOneDayException();
        }
    }

    public Integer checkTime(Long roomId){
        Room room = roomRepository.findById(roomId).orElse(null);
        if(room != null){
            String status = room.getStatus();
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(status.equals(EXTEND)){
                if(hours >= SEVEN_DAY_HOURS){return 7;}
            }else {
                if (hours >= ONE_DAY_HOURS) {return 1;}
            }
            return 0;
        }
        return 9;
    }

    @Scheduled(cron = "0 0 0,12 * * *") // Run at 00, 12 o'clock every day
    public void performScheduledTask(){
        log.info("00, 12 delete Room");
        List<MessageReport> messageReports = messageReportRepository.findAll();
        Set<Long> roomIds = new HashSet<>();
        if(messageReports != null){
            for(MessageReport mr : messageReports){
                roomIds.add(mr.getMessage_id().getRoom().getId());
            }
        }
        List<Room> rooms0;
        if(roomIds.isEmpty()){rooms0 = roomRepository.findByStatus("0");}
        else{rooms0 = roomRepository.findByStatusAndRoomIds("0", roomIds);}
        for(Room room : rooms0){
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(hours > 168){
                log.info("delete : roomId = {}", room.getId());
                deleteMemory(room);
            }
        }
        List<Room> rooms1;
        if(roomIds.isEmpty()){rooms1 = roomRepository.findByStatus("1");}
        else{rooms1 = roomRepository.findByStatusAndRoomIds("1", roomIds);}
        for(Room room : rooms1){
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(hours > 24){
                log.info("delete : roomId = {}", room.getId());
                deleteMemory(room);
            }
        }
    }

    private void deleteMemory(Room room) {
        messageRepository.deleteByRoom(room);
        roomInMemberRepository.deleteByRoom(room);
        roomRepository.deleteById(room.getId());
    }
}