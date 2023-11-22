package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PictureRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("base64Picture")
    @NotBlank
    private String base64Picture;
}
