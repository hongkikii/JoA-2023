package com.mjuAppSW.joA.geography.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주변 사람 목록 화면 사용자 정보 Response")
public class OwnerResponse {
    private Integer status;
    private String name;
    private String urlCode;
    private String bio;
}
