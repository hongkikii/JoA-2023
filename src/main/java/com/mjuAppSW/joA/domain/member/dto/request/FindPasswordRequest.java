package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 찾기 Request")
public class FindPasswordRequest {
    @JsonProperty("loginId")
    @NotBlank
    private String loginId;
}
