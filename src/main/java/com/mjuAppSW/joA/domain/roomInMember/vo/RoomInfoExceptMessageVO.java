package com.mjuAppSW.joA.domain.roomInMember.vo;

import com.mjuAppSW.joA.domain.room.Room;

import java.util.Date;

public interface RoomInfoExceptMessageVO {
    Room getRoom();
    Date getDate();
    String getName();
    String getUrlCode();
}
