package com.mjuAppSW.joA.geography.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class IdRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;
}
