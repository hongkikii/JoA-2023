package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FindIdRequest {
    @JsonProperty("uEmail")
    @NotBlank
    private String uEmail;

    @JsonProperty("collegeId")
    @NotNull
    private Long collegeId;
}
