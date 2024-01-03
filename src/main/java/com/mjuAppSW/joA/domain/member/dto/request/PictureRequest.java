package com.mjuAppSW.joA.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "프로필 사진 변경 Request")
public class PictureRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("base64Picture")
    @NotBlank
    private String base64Picture;
}
