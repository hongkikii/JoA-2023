package com.mjuAppSW.joA.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Integer status;
    private Long id;

    public LoginResponse(Integer status) {
        this.status = status;
        this.id = null;
    }

    public LoginResponse(Integer status, Long id) {
        this.status = status;
        this.id = id;
    }
}
