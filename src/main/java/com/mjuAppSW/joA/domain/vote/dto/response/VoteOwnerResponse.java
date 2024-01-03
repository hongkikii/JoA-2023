package com.mjuAppSW.joA.domain.vote.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "투표 화면 사용자 정보 Response")
public class VoteOwnerResponse {
    private Integer status;
    private String name;
    private String urlCode;

    public VoteOwnerResponse(Integer status) {
        this.status = status;
    }

    public VoteOwnerResponse(Integer status, String name, String urlCode) {
        this.status = status;
        this.name = name;
        this.urlCode = urlCode;
    }
}
