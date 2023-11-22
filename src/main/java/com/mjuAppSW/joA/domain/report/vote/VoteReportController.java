package com.mjuAppSW.joA.domain.report.vote;

import com.mjuAppSW.joA.domain.report.vote.dto.ReportRequest;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VoteReportController {

    private final VoteReportService voteReportService;

    @PostMapping("/vote/report")
    public ResponseEntity<StatusResponse> reportVote(@RequestBody @Valid ReportRequest request) {
        log.info("reportVote : voteId = {}, reportId = {}, content = {}",
                request.getVoteId(), request.getReportId(), request.getContent());
        StatusResponse response = voteReportService.reportVote(request);
        log.info("reportVote Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
