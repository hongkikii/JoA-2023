package com.mjuAppSW.joA.domain.vote.dto;

import lombok.Getter;

@Getter
public class VoteResponse {
    private Integer status;

    public VoteResponse(Integer status) {
        this.status = status;
    }
}
