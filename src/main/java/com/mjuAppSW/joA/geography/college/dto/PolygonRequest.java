package com.mjuAppSW.joA.geography.college.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "학교 범위 등록 Request")
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
