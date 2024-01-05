package com.mjuAppSW.joA.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "비밀번호 찾기 Request")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class FindPasswordRequest {
    @NotBlank
    private final String loginId;
}
