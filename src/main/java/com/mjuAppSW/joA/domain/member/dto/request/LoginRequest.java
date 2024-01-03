package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "로그인 Request")
public class LoginRequest {
    @JsonProperty("loginId")
    @NotBlank
    private String loginId;

    @JsonProperty("password")
    @NotBlank
    private String password;
}
