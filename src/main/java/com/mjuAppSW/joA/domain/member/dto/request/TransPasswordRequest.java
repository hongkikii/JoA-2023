package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 변경 Request")
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
