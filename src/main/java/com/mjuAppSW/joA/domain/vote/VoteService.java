package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.vote.dto.OwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.SendRequest;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import com.mjuAppSW.joA.domain.vote.dto.VoteListResponse;

public interface VoteService {

    StatusResponse sendVote(SendRequest request); // giveId, takeId, categoryId, hint

    VoteListResponse getVotes(Long takeId);

    OwnerResponse getOwnerInfo(Long id);
}
