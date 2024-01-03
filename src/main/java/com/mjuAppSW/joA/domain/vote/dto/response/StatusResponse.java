package com.mjuAppSW.joA.domain.vote.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "상태 코드 Response")
public class StatusResponse {
    private Integer status;

    public StatusResponse(Integer status) {
        this.status = status;
    }
}
