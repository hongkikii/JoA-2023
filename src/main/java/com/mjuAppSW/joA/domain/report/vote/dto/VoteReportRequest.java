package com.mjuAppSW.joA.domain.report.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "투표 신고 Request")
public class VoteReportRequest {
    @JsonProperty("voteId")
    @NotNull
    private Long voteId;

    @JsonProperty("reportId")
    @NotNull
    private Long reportId;

    @JsonProperty("content")
    private String content;
}
