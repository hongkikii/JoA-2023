package com.mjuAppSW.joA.domain.heart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HeartResponse {
    private Integer status;
    private Boolean isMatched;
    private String giveName;
    private String takeName;
    private String giveUrlCode;
    private String takeUrlCode;

    public HeartResponse(Integer status) {
        this.status = status;
    }

    @Builder
    public HeartResponse(Integer status, Boolean isMatched, String giveName, String takeName, String giveUrlCode, String takeUrlCode) {
        this.status = status;
        this.isMatched = isMatched;
        this.giveName = giveName;
        this.takeName = takeName;
        this.giveUrlCode = giveUrlCode;
        this.takeUrlCode = takeUrlCode;
    }
}
