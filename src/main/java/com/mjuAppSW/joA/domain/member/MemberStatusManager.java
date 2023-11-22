package com.mjuAppSW.joA.domain.member;

import static com.mjuAppSW.joA.constant.Constants.EMPTY_STRING;

import com.mjuAppSW.joA.geography.location.LocationRepository;
import com.mjuAppSW.joA.storage.S3Uploader;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberStatusManager {

    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;

    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void check() {
        List<Member> joiningAll = memberRepository.findJoiningAll();
        for (Member member : joiningAll) {
            if (member.getReportCount() == 5 && member.getStatus() == 0) {
                executeStopPolicy(member, member.getReportCount());
            }
            if (member.getReportCount() == 10 && member.getStatus() == 11) {
                executeStopPolicy(member, member.getReportCount());
            }
            if (member.getReportCount() == 15 && member.getStatus() == 22) {
                executeOutPolicy(member);
            }
        }
    }

    private void executeStopPolicy(Member member, int reportCount) {
        if (member.getStopEndDate() == LocalDate.now()) {
           completeStopPolicy(member, reportCount);
           return;
        }
        saveStop(member, reportCount);
    }

    private void saveStop(Member member, int reportCount) {
        LocalDate today = LocalDate.now();
        member.changeStopStartDate(today);
        if (reportCount == 5) {
            member.changeStopEndDate(today.plusDays(1));
        }
        if (reportCount == 10) {
            member.changeStopEndDate(today.plusDays(7));
        }
    }

    private void completeStopPolicy(Member member, int reportCount) {
        if (reportCount == 5) {
            member.changeStatus(11);
        }
        if (reportCount == 10) {
            member.changeStatus(22);
        }
    }

    private void executeOutPolicy(Member member) {
        member.expireSessionId();
        member.changeWithdrawal(true);
        member.changeStatus(3);
        member.changeBasicProfile(true);
        member.changeUrlCode(EMPTY_STRING);
        locationRepository.deleteById(member.getId());
        s3Uploader.deletePicture(member.getUrlCode());
    }
}
