package com.mjuAppSW.joA.domain.member.dto;

import lombok.Getter;

@Getter
public class SetResponse {
    private Integer status;
    private String name;
    private String urlCode;

    public SetResponse(Integer status) {
        this.status = status;
        this.name = "";
        this.urlCode = "";
    }

    public SetResponse(Integer status, String name, String urlCode) {
        this.status = status;
        this.name = name;
        this.urlCode = urlCode;
    }
}
