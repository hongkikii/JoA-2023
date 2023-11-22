package com.mjuAppSW.joA.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SendRequest {
    @JsonProperty("giveId")
    @NotNull
    private Long giveId;

    @JsonProperty("takeId")
    @NotNull
    private Long takeId;

    @JsonProperty("categoryId")
    @NotNull
    private Long categoryId;

    @JsonProperty("hint")
    @Size(max = 15)
    private String hint;
}
