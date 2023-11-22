package com.mjuAppSW.joA.domain.vote.dto;

import lombok.Getter;

@Getter
public class StatusResponse {
    private Integer status;

    public StatusResponse(Integer status) {
        this.status = status;
    }
}
