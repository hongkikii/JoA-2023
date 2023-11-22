package com.mjuAppSW.joA.domain.member.dto;

import lombok.Getter;

@Getter
public class SetResponse {
    private String name;
    private String urlCode;

    public SetResponse(String name, String urlCode) {
        this.name = name;
        this.urlCode = urlCode;
    }
}
