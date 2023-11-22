package com.mjuAppSW.joA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BioRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("bio")
    private String bio;
}
