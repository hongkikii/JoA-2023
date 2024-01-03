package com.mjuAppSW.joA.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "설정 페이지 Response")
public class SetPageResponse {
    private Integer status;
    private String name;
    private String urlCode;

    public SetPageResponse(Integer status) {
        this.status = status;
        this.name = "";
        this.urlCode = "";
    }

    public SetPageResponse(Integer status, String name, String urlCode) {
        this.status = status;
        this.name = name;
        this.urlCode = urlCode;
    }
}
