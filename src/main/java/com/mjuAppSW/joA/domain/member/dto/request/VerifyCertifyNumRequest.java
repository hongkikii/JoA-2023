package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "인증번호 확인 Request")
public class VerifyCertifyNumRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("certifyNum")
    @NotBlank
    private String certifyNum;

    @JsonProperty("uEmail")
    @NotBlank
    private String uEmail;

    @JsonProperty("collegeId")
    @NotNull
    private Long collegeId;
}
