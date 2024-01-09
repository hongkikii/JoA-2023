package com.mjuAppSW.joA.domain.message.dto.vo;

import lombok.Data;

@Data
public class Messages {
    private String content;
    public Messages(String content){
        this.content = content;
    }
}
