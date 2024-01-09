package com.mjuAppSW.joA.domain.roomInMember.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateExpiredRequest {
    @JsonProperty("roomId")
    @NotNull
    private Long roomId;
    @JsonProperty("memberId")
    @NotNull
    private Long memberId;
    @JsonProperty("expired")
    @NotNull
    private String expired;
}
