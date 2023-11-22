package com.mjuAppSW.joA.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class VoteContent {
    @NonNull
    private Long voteId;
    @NonNull
    private Long categoryId;

    private String hint;
}
