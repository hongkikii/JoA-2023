package com.mjuAppSW.joA.domain.vote.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class VoteListResponse {
    private List<VoteContent> voteList;

    public VoteListResponse(List voteList) {
        this.voteList = voteList;
    }
}
