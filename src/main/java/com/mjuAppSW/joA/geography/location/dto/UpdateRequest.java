package com.mjuAppSW.joA.geography.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateRequest {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("latitude")
    @NotNull
    private Double latitude;

    @JsonProperty("longitude")
    @NotNull
    private Double longitude;

    @JsonProperty("altitude")
    @NotNull
    private Double altitude;

    public UpdateRequest(Long id, Double latitude, Double longitude, Double altitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
}
