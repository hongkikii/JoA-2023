package com.mjuAppSW.joA.domain.heart;

import static com.mjuAppSW.joA.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.constant.Constants.Heart.HEART_IS_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Heart.MEMBER_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.Heart.ROOM_IS_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Heart.USER_IS_ANONYMOUS;
import static com.mjuAppSW.joA.constant.Constants.Heart.USER_IS_MATCHED;
import static com.mjuAppSW.joA.constant.Constants.Heart.USER_IS_NAMED;
import static com.mjuAppSW.joA.constant.Constants.NORMAL_OPERATION;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberAccessor;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMember;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartServiceImpl implements HeartService {

    private final HeartRepository heartRepository;
    private final RoomInMemberRepository roomInMemberRepository;
    private final MemberAccessor memberAccessor;

    @Transactional
    public HeartResponse sendHeart(HeartRequest request) {
        Long giveSessionId = request.getGiveId();
        Long takeMemberId = request.getTakeId();
        boolean isNamed = request.getNamed();
        Member giveMember = memberAccessor.findBySessionId(giveSessionId);
        Member takeMember = memberAccessor.findById(takeMemberId);
        Long giveMemberId = giveMember.getId();

        if (isNull(giveMember) || isNull(takeMember))
            return new HeartResponse(MEMBER_IS_INVALID);

        Heart equalHeart = findEqualByIdAndDate(giveMemberId, takeMemberId);
        if (!isNull(equalHeart))
            return new HeartResponse(HEART_IS_EXISTED);

        Heart heart = makeHeart(giveMemberId, takeMember, isNamed);
        heartRepository.save(heart);

        List<RoomInMember> rooms = roomInMemberRepository.checkRoomInMember(giveMember, takeMember);
        if (rooms.size() != 0) {
            return new HeartResponse(ROOM_IS_EXISTED);
        }

        if(isOpponentHeartExisted(takeMemberId, giveMemberId))
            return makeResponse(USER_IS_MATCHED, giveMember, takeMember);
        if (isNamed)
            return makeResponse(USER_IS_NAMED, giveMember, takeMember);

        return makeResponse(USER_IS_ANONYMOUS, giveMember, takeMember);
    }

    private Heart findEqualByIdAndDate(Long giveId, Long takeId) {
        return heartRepository.findEqualHeart(LocalDate.now(), giveId, takeId).orElse(null);
    }

    private Heart makeHeart(Long giveId, Member takeMember, Boolean named) {
        return Heart.builder()
                .giveId(giveId).member(takeMember)
                .date(LocalDate.now()).named(named)
                .build();
    }

    private Boolean isOpponentHeartExisted(Long takeId, Long giveId) {
        Heart opponentHeart = findEqualByIdAndDate(takeId, giveId);
        if(opponentHeart != null)
            return true;
        return false;
    }

    private HeartResponse makeResponse(Integer result, Member... members) {
        Member giveMember = members[0];
        Member takeMember = members[1];

        String giveName = EMPTY_STRING;
        String giveUrlCode = EMPTY_STRING;
        String takeName = EMPTY_STRING;
        String takeUrlCode = EMPTY_STRING;
        Boolean isMatched = false;

        if (result == USER_IS_MATCHED) {
            giveName = giveMember.getName();
            giveUrlCode = giveMember.getUrlCode();
            takeName = takeMember.getName();
            takeUrlCode = takeMember.getUrlCode();
            isMatched = true;
        }

        if (result == USER_IS_NAMED) {
            giveName = giveMember.getName();
            giveUrlCode = giveMember.getUrlCode();
        }

        // result == USER_IS_ANONYMOUS

        return HeartResponse.builder()
                .status(NORMAL_OPERATION).isMatched(isMatched)
                .giveName(giveName).giveUrlCode(giveUrlCode)
                .takeName(takeName).takeUrlCode(takeUrlCode)
                .build();
    }
}
