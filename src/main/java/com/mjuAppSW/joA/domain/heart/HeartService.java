package com.mjuAppSW.joA.domain.heart;

import com.mjuAppSW.joA.common.auth.MemberChecker;
import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;
import com.mjuAppSW.joA.geography.block.exception.BlockAccessForbiddenException;
import com.mjuAppSW.joA.domain.heart.exception.HeartAlreadyExistedException;
import com.mjuAppSW.joA.domain.heart.exception.RoomExistedException;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.member.exception.MemberNotFoundException;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
import com.mjuAppSW.joA.geography.block.BlockRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartService {

    private final HeartRepository heartRepository;
    private final RoomInMemberRepository roomInMemberRepository;
    private final BlockRepository blockRepository;
    private final MemberChecker memberChecker;

    @Transactional
    public HeartResponse sendHeart(HeartRequest request) {
        Member giveMember = memberChecker.findBySessionId(request.getGiveId());
        Long giveMemberId = giveMember.getId();
        Long takeMemberId = request.getTakeId();
        Member takeMember = memberChecker.findById(takeMemberId);

        checkBlock(giveMemberId, takeMemberId);
        checkEqualHeart(giveMemberId, takeMemberId);

        Heart heart = makeHeart(giveMemberId, takeMember);
        heartRepository.save(heart);

        checkAlreadyRoom(giveMember, takeMember);

        Boolean isMatched = isOpponentHeartExisted(takeMemberId, giveMemberId);
        return HeartResponse.of(isMatched, giveMember, takeMember);
    }

    private void checkBlock(Long giveMemberId, Long takeMemberId) {
        if (!blockRepository.findBlockByIds(takeMemberId, giveMemberId).isEmpty()) {
            throw new BlockAccessForbiddenException();
        }
    }

    private void checkEqualHeart(Long giveId, Long takeId) {
        heartRepository.findEqualHeart(LocalDate.now(), giveId, takeId)
                .orElseThrow(HeartAlreadyExistedException::new);
    }

    private Heart makeHeart(Long giveId, Member takeMember) {
        return Heart.builder()
                .giveId(giveId)
                .member(takeMember)
                .date(LocalDate.now())
                .build();
    }

    private void checkAlreadyRoom(Member giveMember, Member takeMember) {
        if (!roomInMemberRepository.checkRoomInMember(giveMember, takeMember).isEmpty()) {
            throw new RoomExistedException();
        }
    }

    private Boolean isOpponentHeartExisted(Long takeId, Long giveId) {
        return heartRepository.findEqualHeart(LocalDate.now(), takeId, giveId).isPresent();
    }
}
