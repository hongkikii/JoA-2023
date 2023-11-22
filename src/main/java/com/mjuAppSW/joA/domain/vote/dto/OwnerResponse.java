package com.mjuAppSW.joA.domain.vote.dto;

import lombok.Getter;

@Getter
public class OwnerResponse {
    private Integer status;
    private String name;
    private String urlCode;

    public OwnerResponse(Integer status) {
        this.status = status;
    }

    public OwnerResponse(Integer status, String name, String urlCode) {
        this.status = status;
        this.name = name;
        this.urlCode = urlCode;
    }
}
