package com.mjuAppSW.joA.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 Response")
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
