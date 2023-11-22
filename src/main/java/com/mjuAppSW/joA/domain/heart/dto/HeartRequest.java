package com.mjuAppSW.joA.domain.heart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
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
