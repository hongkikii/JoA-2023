package com.mjuAppSW.joA.domain.vote.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "투표 전송 Response")
public class SendVoteRequest {
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
