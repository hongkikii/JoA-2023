package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransPasswordRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("currentPassword")
    @NotBlank
    private String currentPassword;

    @JsonProperty("newPassword")
    @NotBlank
    private String newPassword;
}
