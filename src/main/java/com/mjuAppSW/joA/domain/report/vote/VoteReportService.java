package com.mjuAppSW.joA.domain.report.vote;

import static com.mjuAppSW.joA.constant.Constants.NORMAL_OPERATION;
import static com.mjuAppSW.joA.constant.Constants.ReportVote.EQUAL_VOTE_REPORT_IS_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.ReportVote.REPORT_CATEGORY_IS_NOT_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.ReportVote.VOTE_IS_NOT_EXISTED;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import com.mjuAppSW.joA.domain.report.vote.dto.ReportRequest;
import com.mjuAppSW.joA.domain.vote.Vote;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteReportService {
    private final VoteRepository voteRepository;
    private final VoteReportRepository voteReportRepository;
    private final ReportCategoryRepository reportCategoryRepository;

    @Transactional
    public StatusResponse reportVote(ReportRequest request) {
        Long voteId = request.getVoteId();
        Vote vote = findVoteById(voteId);
        ReportCategory reportCategory = findReportCategoryById(request.getReportId());
        if (isNull(vote))
            return new StatusResponse(VOTE_IS_NOT_EXISTED);
        if (isNull(reportCategory))
            return new StatusResponse(REPORT_CATEGORY_IS_NOT_EXISTED);

        VoteReport equalReport = findReportByVoteId(voteId);
        if(!isNull(equalReport))
            return new StatusResponse(EQUAL_VOTE_REPORT_IS_EXISTED);

        VoteReport voteReport = makeVoteReport(vote, reportCategory, request.getContent());
        voteReportRepository.save(voteReport);
        return new StatusResponse(NORMAL_OPERATION);
    }

    private VoteReport findReportByVoteId(Long id) {
        return voteReportRepository.findByVoteId(id).orElse(null);
    }

    private ReportCategory findReportCategoryById(Long id) {
        return reportCategoryRepository.findById(id).orElse(null);
    }

    private Vote findVoteById(Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    private VoteReport makeVoteReport(Vote vote, ReportCategory reportCategory, String content) {
        return new VoteReport(vote, reportCategory, content, LocalDateTime.now());
    }
}
