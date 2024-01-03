package com.mjuAppSW.joA.domain.member.profile;

import static com.mjuAppSW.joA.common.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.common.constant.Constants.NORMAL_OPERATION;
import static com.mjuAppSW.joA.common.constant.Constants.S3Uploader.ERROR;
import static com.mjuAppSW.joA.common.constant.Constants.S3Uploader.S3_UPLOAD_IS_INVALID;
import static com.mjuAppSW.joA.common.constant.Constants.TransPicture;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberAccessor;
import com.mjuAppSW.joA.domain.member.dto.request.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.response.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.request.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SetPageResponse;
import com.mjuAppSW.joA.domain.member.dto.response.StatusResponse;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.common.storage.S3Uploader;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberAccessor memberAccessor;
    private final HeartRepository heartRepository;
    private final VoteRepository voteRepository;
    private final SessionManager sessionManager;
    private final S3Uploader s3Uploader;

    public SetPageResponse getSettingPage(Long id) {
        Member member = findBySessionId(id);
        if (isNull(member))
            return new SetPageResponse(1);

        if (memberAccessor.isStopped(member.getSessionId()))
            return new SetPageResponse(2);

        String urlCode = EMPTY_STRING;
        if (!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return new SetPageResponse(0, member.getName(), urlCode);
    }

    public MyPageResponse getMyPage(Long sessionId) {
        Member member = findBySessionId(sessionId);
        if (isNull(member))
            return null;

        if(memberAccessor.isStopped(member.getSessionId()))
            return null;

        int todayHeart = heartRepository.countTodayHeartsById(LocalDate.now(), member.getId());
        int totalHeart = heartRepository.countTotalHeartsById(member.getId());
        List<String> voteTop3 = voteRepository.findVoteCategoryById(member.getId(), top3Page());

        String urlCode = EMPTY_STRING;
        if (!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return MyPageResponse.builder().name(member.getName())
                .bio(member.getBio())
                .urlCode(urlCode)
                .todayHeart(todayHeart)
                .totalHeart(totalHeart)
                .voteTop3(voteTop3).build();
    }

    @Transactional
    public StatusResponse transBio(BioRequest request) {
        Member member = findBySessionId(request.getId());
        if (isNull(member))
            return new StatusResponse(1);

        member.changeBio(request.getBio());
        return new StatusResponse(0);
    }

    @Transactional
    public StatusResponse deleteBio(SessionIdRequest request) {
        Member member = findBySessionId(request.getId());
        if (isNull(member))
            return new StatusResponse(1);

        member.changeBio("");
        return new StatusResponse(0);
    }

    @Transactional
    public StatusResponse transPicture(PictureRequest request) {
        Member member = findBySessionId(request.getId());
        if (isNull(member))
            return new StatusResponse(TransPicture.MEMBER_IS_NOT_EXISTED);

        if (!member.getBasicProfile())
            s3Uploader.deletePicture(member.getUrlCode());

        String newUrlCode = s3Uploader.putPicture(member.getId(), request.getBase64Picture());
        member.changeUrlCode(newUrlCode);

        if (!newUrlCode.equals(ERROR)) {
            if (member.getBasicProfile())
                member.changeBasicProfile(false);
            return new StatusResponse(NORMAL_OPERATION);
        }
        return new StatusResponse(S3_UPLOAD_IS_INVALID);
    }

    @Transactional
    public StatusResponse deletePicture(SessionIdRequest request) {
        Member member = findBySessionId(request.getId());
        if (isNull(member))
            return new StatusResponse(1);

        s3Uploader.deletePicture(member.getUrlCode());
        member.changeUrlCode(EMPTY_STRING);
        member.changeBasicProfile(true);
        return new StatusResponse(0);
    }

    private Member findBySessionId(Long sessionId) {
        return sessionManager.findBySessionId(sessionId);
    }

    private Pageable top3Page() {
        return PageRequest.of(0, 3);
    }
}
