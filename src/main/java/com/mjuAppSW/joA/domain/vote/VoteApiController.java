package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.vote.dto.OwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.SendRequest;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import com.mjuAppSW.joA.domain.vote.dto.VoteListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VoteApiController {
    private final VoteServiceImpl voteService;

    @PostMapping("/vote/send")
    public ResponseEntity<StatusResponse> sendVote(@RequestBody @Valid SendRequest request) {
        log.info("sendVote : giveId = {}, takeId = {}, categoryId = {}, hint = {}",
                request.getGiveId(), request.getTakeId(), request.getCategoryId(), request.getHint());
        StatusResponse response = voteService.sendVote(request);
        log.info("sendVote Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vote/get")
    public ResponseEntity<VoteListResponse> getVotes(@RequestParam("takeId") Long takeId) {
        log.info("getVotes : takeId = {}", takeId);
        VoteListResponse response = voteService.getVotes(takeId);
        if (response != null) {
            log.info("getVotes Return : Ok, voteList size = {}", response.getVoteList().size());
            return ResponseEntity.ok(response);
        }
        else {
            log.warn("getVotes Return : BAD_REQUEST, member id is not valid");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vote/get/owner")
    public ResponseEntity<OwnerResponse> getVoteOwnerInfo(@RequestParam Long id) {
        log.info("getVoteOwnerInfo : id = {}", id);
        OwnerResponse response = voteService.getOwnerInfo(id);
        if (response.getStatus() == 0) {
            log.info("getVoteOwnerInfo Return : OK, status = {}, name = {}, urlCode = {}",
                    response.getStatus(), response.getName(), response.getUrlCode());
        }
        else {
            log.info("getVoteOwnerInfo Return : OkK, status = {}", response.getStatus());
        }
        return ResponseEntity.ok(response);
    }
}
