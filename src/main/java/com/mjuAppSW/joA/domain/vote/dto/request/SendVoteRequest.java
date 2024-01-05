package com.mjuAppSW.joA.domain.vote.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "투표 전송 Request")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SendVoteRequest {
    @NotNull
    private Long giveId;
    @NotNull
    private Long takeId;
    @NotNull
    private Long categoryId;
    @Size(max = 15)
    private String hint;
}
