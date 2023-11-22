package com.mjuAppSW.joA.geography.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PolygonRequest {
    @NotNull
    private Long collegeId;
    @NotNull
    private Double topLeftLng;
    @NotNull
    private Double topLeftLat;
    @NotNull
    private Double topRightLng;
    @NotNull
    private Double topRightLat;
    @NotNull
    private Double bottomRightLng;
    @NotNull
    private Double bottomRightLat;
    @NotNull
    private Double bottomLeftLng;
    @NotNull
    private Double bottomLeftLat;
}
