package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "회원 가입 Request")
public class JoinRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("loginId")
    @NotBlank
    private String loginId;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("password")
    @NotBlank
    private String password;

}
