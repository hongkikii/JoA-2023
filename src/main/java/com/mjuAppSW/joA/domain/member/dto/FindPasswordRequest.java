package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindPasswordRequest {
    @JsonProperty("loginId")
    @NotBlank
    private String loginId;
}
