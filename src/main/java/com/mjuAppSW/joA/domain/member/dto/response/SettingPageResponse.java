package com.mjuAppSW.joA.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "설정 페이지 Response")
public class SettingPageResponse {
    private final String name;
    private final String urlCode;

    public SettingPageResponse(String name, String urlCode) {
        this.name = name;
        this.urlCode = urlCode;
    }
}
