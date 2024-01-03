package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.vote.dto.response.VoteOwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.request.SendVoteRequest;
import com.mjuAppSW.joA.domain.vote.dto.response.StatusResponse;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteListResponse;

public interface VoteService {

    StatusResponse sendVote(SendVoteRequest request); // giveId, takeId, categoryId, hint

    VoteListResponse getVotes(Long takeId);

    VoteOwnerResponse getVoteOwner(Long id);
}
