package com.mjuAppSW.joA.domain.vote.dto.response;

import com.mjuAppSW.joA.domain.vote.dto.response.VoteContent;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "받은 투표 목록 Response")
public class VoteListResponse {
    private List<VoteContent> voteList;

    public VoteListResponse(List voteList) {
        this.voteList = voteList;
    }
}
