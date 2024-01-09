package com.mjuAppSW.joA.domain.roomInMember.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomListVO {
    private Long roomId;
    private String name;
    private String urlCode;
    private String content;
    private String unCheckedMessage;

    @Builder
    public RoomListVO(Long roomId, String name, String urlCode, String content, String unCheckedMessage) {
        this.roomId = roomId;
        this.name = name;
        this.urlCode = urlCode;
        this.content = content;
        this.unCheckedMessage = unCheckedMessage;
    }
}
