package com.mjuAppSW.joA.domain.vote;

import static com.mjuAppSW.joA.constant.Constants.NORMAL_OPERATION;
import static com.mjuAppSW.joA.constant.Constants.Vote.GIVE_MEMBER_CANNOT_SEND_TO_OPPONENT;
import static com.mjuAppSW.joA.constant.Constants.Vote.MEMBER_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.Vote.VOTE_CATEGORY_IS_NOT_VALID;
import static com.mjuAppSW.joA.constant.Constants.Vote.VOTE_IS_EXISTED;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberAccessor;
import com.mjuAppSW.joA.domain.vote.dto.OwnerResponse;
import com.mjuAppSW.joA.domain.vote.dto.SendRequest;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import com.mjuAppSW.joA.domain.vote.dto.VoteContent;
import com.mjuAppSW.joA.domain.vote.dto.VoteListResponse;
import com.mjuAppSW.joA.domain.voteCategory.VoteCategory;
import com.mjuAppSW.joA.domain.voteCategory.VoteCategoryRepository;
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
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final VoteCategoryRepository voteCategoryRepository;
    private final MemberAccessor memberAccessor;

    @Transactional
    public StatusResponse sendVote(SendRequest request) {
        Member giveMember = memberAccessor.findBySessionId(request.getGiveId());
        Member takeMember = memberAccessor.findById(request.getTakeId());
        VoteCategory voteCategory = findVoteCategoryById(request.getCategoryId());

        if(isNull(giveMember) || isNull(takeMember))
            return new StatusResponse(MEMBER_IS_INVALID);
        if(isNull(voteCategory))
            return new StatusResponse(VOTE_CATEGORY_IS_NOT_VALID);

        Vote equalVote = findEqualVote(giveMember.getId(), takeMember.getId(), voteCategory.getId());
        if (!isNull(equalVote))
            return new StatusResponse(VOTE_IS_EXISTED);

        List<Vote> invalidVotes = voteRepository.findInvalidVotes(giveMember.getId(),takeMember.getId());
        if (invalidVotes.size() != 0)
            return new StatusResponse(GIVE_MEMBER_CANNOT_SEND_TO_OPPONENT);

        Vote vote = makeVote(giveMember, takeMember, voteCategory, request.getHint());
        voteRepository.save(vote);
        return new StatusResponse(NORMAL_OPERATION);
    }

    public VoteListResponse getVotes(Long sessionId) {
        Member findTakeMember = memberAccessor.findBySessionId(sessionId);
        if(isNull(findTakeMember))
            return null;

        List<VoteContent> voteList = getVoteList(findTakeMember.getId());
        return new VoteListResponse(voteList);
    }

    public OwnerResponse getOwnerInfo(Long id) {
        Member findMember = memberAccessor.findBySessionId(id);
        if(findMember == null)
            return new OwnerResponse(1);
        return new OwnerResponse(NORMAL_OPERATION, findMember.getName(), findMember.getUrlCode());
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

    private VoteContent makeVoteContent(Vote vote) {
        return VoteContent.builder()
                        .voteId(vote.getId())
                        .categoryId(vote.getVoteCategory().getId())
                        .hint(vote.getHint())
                        .build();
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

    private VoteCategory findVoteCategoryById(Long id) {
        return voteCategoryRepository.findById(id).orElse(null);
    }

    private List<Vote> findVotesByTakeId(Long id, Pageable pageable) {
        return voteRepository.findValidAllByTakeId(id, pageable);
    }

    private Vote findEqualVote(Long giveId, Long takeId, Long categoryId) {
        return voteRepository.findTodayEqualVote(giveId, takeId, categoryId, LocalDate.now()).orElse(null);
    }
}
