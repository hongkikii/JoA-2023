package com.mjuAppSW.joA.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "프로필 사진 변경 Request")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PictureRequest {
    @NotNull
    private final Long id;
    @NotBlank
    private String base64Picture;
}
