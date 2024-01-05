package com.mjuAppSW.joA.domain.heart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "하트 전송 Request")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class HeartRequest {
    @NotNull
    private Long giveId;
    @NotNull
    private Long takeId;
    @NotNull
    private Boolean named;
}
