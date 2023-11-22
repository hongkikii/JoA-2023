package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @JsonProperty("loginId")
    @NotBlank
    private String loginId;

    @JsonProperty("password")
    @NotBlank
    private String password;
}
