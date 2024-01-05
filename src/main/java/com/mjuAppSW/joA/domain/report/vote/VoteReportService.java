package com.mjuAppSW.joA.domain.report.vote;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.member.exception.MemberNotFoundException;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import com.mjuAppSW.joA.domain.report.vote.dto.VoteReportRequest;
import com.mjuAppSW.joA.domain.report.vote.exception.ReportCategoryNotFoundException;
import com.mjuAppSW.joA.domain.report.vote.exception.VoteNotFoundException;
import com.mjuAppSW.joA.domain.report.vote.exception.VoteReportAlreadyExistedException;
import com.mjuAppSW.joA.domain.vote.Vote;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void reportVote(VoteReportRequest request) {
        ReportCategory reportCategory = findReportCategoryById(request.getReportId());
        Long voteId = request.getVoteId();
        Vote vote = findVoteById(voteId);
        Member giveMember = memberRepository.findById(vote.getGiveId()).orElseThrow(MemberNotFoundException::new);

        checkEqualReport(voteId);

        VoteReport voteReport = makeVoteReport(vote, reportCategory, request.getContent());
        voteReportRepository.save(voteReport);
        vote.changeInvalid();
        giveMember.addReportCount();
    }

    private ReportCategory findReportCategoryById(Long id) {
        return reportCategoryRepository.findById(id).orElseThrow(ReportCategoryNotFoundException::new);
    }

    private Vote findVoteById(Long id) {
        return voteRepository.findById(id).orElseThrow(VoteNotFoundException::new);
    }

    private void checkEqualReport(Long voteId) {
        Optional<VoteReport> voteReport = voteReportRepository.findByVoteId(voteId);
        if (voteReport.isPresent()) {
            throw new VoteReportAlreadyExistedException();
        }
    }

    private VoteReport makeVoteReport(Vote vote, ReportCategory reportCategory, String content) {
        return new VoteReport(vote, reportCategory, content, LocalDateTime.now());
    }
}
