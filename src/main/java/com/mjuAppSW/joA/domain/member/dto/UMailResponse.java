package com.mjuAppSW.joA.domain.member.dto;

import lombok.Getter;

@Getter
public class UMailResponse {
    private Integer status;
    private Long id;

    public UMailResponse(Integer status) {
        this.status = status;
        this.id = null;
    }

    public UMailResponse(Integer status, Long id) {
        this.status = status;
        this.id = id;
    }
 }