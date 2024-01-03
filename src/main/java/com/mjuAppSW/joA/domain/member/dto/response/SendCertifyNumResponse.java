package com.mjuAppSW.joA.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "인증번호 전송 Response")
public class SendCertifyNumResponse {
    private Integer status;
    private Long id;

    public SendCertifyNumResponse(Integer status) {
        this.status = status;
        this.id = null;
    }

    public SendCertifyNumResponse(Integer status, Long id) {
        this.status = status;
        this.id = id;
    }
 }
