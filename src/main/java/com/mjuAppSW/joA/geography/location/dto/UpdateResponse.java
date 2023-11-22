package com.mjuAppSW.joA.geography.location.dto;

import lombok.Getter;

@Getter
public class UpdateResponse {
    private Integer status;
    private Boolean isContained;

    public UpdateResponse(Integer status, Boolean isContained) {
        this.status = status;
        this.isContained = isContained;
    }
}
