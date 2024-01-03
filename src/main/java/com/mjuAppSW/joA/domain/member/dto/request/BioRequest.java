package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "한 줄 소개 변경 Request")
public class BioRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("bio")
    private String bio;
}
