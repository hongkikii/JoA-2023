package com.mjuAppSW.joA.common.session;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.member.exception.AccessForbiddenException;
import com.mjuAppSW.joA.domain.member.exception.MemberNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionManager {

    private final MemberRepository memberRepository;

    public long makeSessionId() {
        long min = 1000000000L;
        long max = 9999999999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?")
    public void expiredSessionId() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            member.makeSessionId(makeSessionId());
        }
    }

    public Member findBySessionId(Long sessionId) {
        return memberRepository.findBysessionId(sessionId)
                .filter(member -> {
                    if (member.getIsWithdrawal()) {
                        throw new MemberNotFoundException();
                    }
                    if (member.getStatus() == 1 || member.getStatus() == 2) {
                        throw new AccessForbiddenException();
                    }
                    return true;
                })
                .orElseThrow(MemberNotFoundException::new);
    }
}
