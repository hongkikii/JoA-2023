package com.mjuAppSW.joA.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportRequest {
    @JsonProperty("voteId")
    @NotNull
    private Long voteId;

    @JsonProperty("reportId")
    @NotNull
    private Long reportId;

    @JsonProperty("content")
    private String content;
}
