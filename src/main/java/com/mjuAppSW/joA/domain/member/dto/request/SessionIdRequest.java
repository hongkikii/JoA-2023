package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "사용자 세션 id Request")
public class SessionIdRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;
}
