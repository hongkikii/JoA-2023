package com.mjuAppSW.joA.geography.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 위치 업데이트 Response")
public class UpdateResponse {
    private Integer status;
    private Boolean isContained;

    public UpdateResponse(Integer status, Boolean isContained) {
        this.status = status;
        this.isContained = isContained;
    }
}
