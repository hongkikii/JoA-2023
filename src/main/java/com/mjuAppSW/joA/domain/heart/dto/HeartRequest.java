package com.mjuAppSW.joA.domain.heart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "하트 전송 Request")
public class HeartRequest {
    @JsonProperty("giveId")
    @NotNull
    private Long giveId;

    @JsonProperty("takeId")
    @NotNull
    private Long takeId;

    @JsonProperty("named")
    @NotNull
    private Boolean named;
}
