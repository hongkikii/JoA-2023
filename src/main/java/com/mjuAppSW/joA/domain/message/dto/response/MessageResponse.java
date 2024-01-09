package com.mjuAppSW.joA.domain.message.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import com.mjuAppSW.joA.domain.message.dto.vo.Messages;

@Getter
@Schema(description = "채팅 목록 Response")
public class MessageResponse {
    private List<Messages> messageResponseList;

    @Builder
    public MessageResponse(List<Messages> messageResponseList){
        this.messageResponseList = messageResponseList;
    }

    public static MessageResponse of(List<Messages> messageResponseList){
        return MessageResponse.builder()
            .messageResponseList(messageResponseList)
            .build();
    }
}
