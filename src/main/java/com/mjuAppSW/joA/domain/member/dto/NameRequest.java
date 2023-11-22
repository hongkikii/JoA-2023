package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NameRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("name")
    @NotBlank
    private String name;
}
