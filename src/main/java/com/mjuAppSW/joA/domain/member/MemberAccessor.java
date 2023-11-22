package com.mjuAppSW.joA.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAccessor {

    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null)
            return null;
        return member;
    }

    public Member findBySessionId(Long sessionId) {
        Member member = memberRepository.findBysessionId(sessionId).orElse(null);
        if(member == null)
            return null;
        return member;
    }

    public boolean isStopped(Long sessionId) {
        Member member = findBySessionId(sessionId);
        if (member == null) {
            return false;
        }
        if (member.getStatus() == 1 && member.getStatus() == 2) {
            return true;
        }
        return false;
    }
}
