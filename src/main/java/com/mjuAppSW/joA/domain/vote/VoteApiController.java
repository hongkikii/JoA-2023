package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.vote.dto.request.SendVoteRequest;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteOwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.response.StatusResponse;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/joa/votes")
public class VoteApiController {
    private final VoteServiceImpl voteService;

    @Operation(summary = "투표 전송", description = "투표 전송 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 전송 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/send")
    public ResponseEntity<StatusResponse> sendVote(@RequestBody @Valid SendVoteRequest request) {
        log.info("sendVote : giveId = {}, takeId = {}, categoryId = {}, hint = {}",
                request.getGiveId(), request.getTakeId(), request.getCategoryId(), request.getHint());
        StatusResponse response = voteService.sendVote(request);
        log.info("sendVote Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "받은 투표 목록 조회", description = "받은 투표 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "받은 투표 목록 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<VoteListResponse> getVotes(
            @Parameter(description = "투표 수신자 세션 id", in = ParameterIn.QUERY)
            @RequestParam Long takeId) {

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

    @Operation(summary = "투표 화면 사용자 정보 조회", description = "투표 화면 사용자 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 전송 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/owner")
    public ResponseEntity<VoteOwnerResponse> getVoteOwner(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.QUERY) @RequestParam Long sessionId) {

        log.info("getVoteOwnerInfo : sessionId = {}", sessionId);
        VoteOwnerResponse response = voteService.getVoteOwner(sessionId);
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
