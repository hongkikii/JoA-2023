package com.mjuAppSW.joA.geography.location.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(description = "사용자 위치 업데이트 Request")
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
