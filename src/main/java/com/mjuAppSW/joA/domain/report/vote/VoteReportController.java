package com.mjuAppSW.joA.domain.report.vote;

import com.mjuAppSW.joA.domain.report.vote.dto.VoteReportRequest;
import com.mjuAppSW.joA.domain.vote.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/joa/reports")
public class VoteReportController {

    private final VoteReportService voteReportService;

    @Operation(summary = "투표 신고", description = "투표 신고 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 신고 확인 코드 응답"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/vote")
    public ResponseEntity<StatusResponse> reportVote(@RequestBody @Valid VoteReportRequest request) {
        log.info("reportVote : voteId = {}, reportId = {}, content = {}",
                request.getVoteId(), request.getReportId(), request.getContent());
        StatusResponse response = voteReportService.reportVote(request);
        log.info("reportVote Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
