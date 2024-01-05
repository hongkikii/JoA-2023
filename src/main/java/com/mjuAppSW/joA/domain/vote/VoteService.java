package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.geography.block.exception.BlockAccessForbiddenException;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.member.exception.AccessForbiddenException;
import com.mjuAppSW.joA.domain.member.exception.MemberNotFoundException;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteOwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.request.SendVoteRequest;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteContent;
import com.mjuAppSW.joA.domain.vote.dto.response.VoteListResponse;
import com.mjuAppSW.joA.domain.vote.exception.VoteAlreadyExistedException;
import com.mjuAppSW.joA.domain.vote.exception.VoteCategoryNotFoundException;
import com.mjuAppSW.joA.domain.vote.voteCategory.VoteCategory;
import com.mjuAppSW.joA.domain.vote.voteCategory.VoteCategoryRepository;
import com.mjuAppSW.joA.geography.block.BlockRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteCategoryRepository voteCategoryRepository;
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @Transactional
    public void sendVote(SendVoteRequest request) {
        Member giveMember = sessionManager.findBySessionId(request.getGiveId());
        Member takeMember = memberRepository.findById(request.getTakeId()).orElseThrow(MemberNotFoundException::new);
        VoteCategory voteCategory = findVoteCategoryById(request.getCategoryId());

        Long giveMemberId = giveMember.getId();
        Long takeMemberId = takeMember.getId();

        checkEqualVote(giveMemberId, takeMemberId, voteCategory.getId());
        checkInvalidVote(giveMemberId, takeMemberId);
        checkBlock(giveMemberId, takeMemberId);

        Vote vote = makeVote(giveMember, takeMember, voteCategory, request.getHint());
        voteRepository.save(vote);
    }

    public VoteListResponse getVotes(Long sessionId) {
        Member findTakeMember = sessionManager.findBySessionId(sessionId);
        return VoteListResponse.of(getVoteList(findTakeMember.getId()));
    }

    public VoteOwnerResponse getVoteOwner(Long sessionId) {
        return VoteOwnerResponse.of(sessionManager.findBySessionId(sessionId));
    }

    private VoteCategory findVoteCategoryById(Long id) {
        return voteCategoryRepository.findById(id).orElseThrow(VoteCategoryNotFoundException::new);
    }

    private void checkEqualVote(Long giveId, Long takeId, Long categoryId) {
        voteRepository.findTodayEqualVote(giveId, takeId, categoryId, LocalDate.now())
                .orElseThrow(VoteAlreadyExistedException::new);
    }

    private void checkInvalidVote(Long giveId, Long takeId) {
        if (!voteRepository.findInvalidVotes(giveId, takeId).isEmpty()) {
            throw new AccessForbiddenException();
        }
    }

    private void checkBlock(Long giveId, Long takeId) {
        if (!blockRepository.findBlockByIds(giveId, takeId).isEmpty()) {
            throw new BlockAccessForbiddenException();
        }
    }

    private Vote makeVote(Member giveMember, Member takeMember, VoteCategory voteCategory, String hint) {
        return Vote.builder()
                .giveId(giveMember.getId())
                .member(takeMember)
                .voteCategory(voteCategory)
                .date(LocalDate.now())
                .hint(hint)
                .build();
    }

    private List<VoteContent> getVoteList(Long id) {
        List<VoteContent> voteList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 30);
        List<Vote> votes = findVotesByTakeId(id, pageable);
        for (Vote vote : votes) {
            voteList.add(makeVoteContent(vote));
        }
        return voteList;
    }

    private List<Vote> findVotesByTakeId(Long id, Pageable pageable) {
        return voteRepository.findValidAllByTakeId(id, pageable);
    }

    private VoteContent makeVoteContent(Vote vote) {
        return VoteContent.builder()
                        .voteId(vote.getId())
                        .categoryId(vote.getVoteCategory().getId())
                        .hint(vote.getHint())
                        .build();
    }
}
