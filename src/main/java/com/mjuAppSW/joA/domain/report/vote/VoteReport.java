package com.mjuAppSW.joA.domain.report.vote;

import java.time.LocalDateTime;

import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.vote.Vote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteReport {

    @Id @GeneratedValue
    @Column(name = "Report_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_id", nullable = false)
    private ReportCategory reportCategory;

    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    public VoteReport(Vote vote, ReportCategory reportCategory, String content, LocalDateTime date) {
        this.vote = vote;
        this.reportCategory = reportCategory;
        this.content = content;
        this.date = date;
    }
}
