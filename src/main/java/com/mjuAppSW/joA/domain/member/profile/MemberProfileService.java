package com.mjuAppSW.joA.domain.member.profile;

import static com.mjuAppSW.joA.common.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.common.constant.Constants.S3Uploader.ERROR;

import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.dto.request.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.response.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.request.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SettingPageResponse;
import com.mjuAppSW.joA.domain.member.exception.S3InvalidException;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.common.storage.S3Uploader;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final HeartRepository heartRepository;
    private final VoteRepository voteRepository;
    private final SessionManager sessionManager;
    private final S3Uploader s3Uploader;

    public SettingPageResponse getSettingPage(Long sessionId) {
        Member member = sessionManager.findBySessionId(sessionId);

        String urlCode = EMPTY_STRING;
        if (!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return new SettingPageResponse(member.getName(), urlCode);
    }

    public MyPageResponse getMyPage(Long sessionId) {
        Member member = sessionManager.findBySessionId(sessionId);

        int todayHeart = heartRepository.countTodayHeartsById(LocalDate.now(), member.getId());
        int totalHeart = heartRepository.countTotalHeartsById(member.getId());
        List<String> voteTop3 = voteRepository.findVoteCategoryById(member.getId(), PageRequest.of(0, 3));

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
    public void transBio(BioRequest request) {
        Member member = sessionManager.findBySessionId(request.getId());
        member.changeBio(request.getBio());
    }

    @Transactional
    public void deleteBio(SessionIdRequest request) {
        Member member = sessionManager.findBySessionId(request.getId());
        member.changeBio("");
    }

    @Transactional
    public void transPicture(PictureRequest request) {
        Member member = sessionManager.findBySessionId(request.getId());

        if (!member.getBasicProfile())
            s3Uploader.deletePicture(member.getUrlCode());

        String newUrlCode = s3Uploader.putPicture(member.getId(), request.getBase64Picture());
        if(newUrlCode.equals(ERROR))
            throw new S3InvalidException();

        member.changeUrlCode(newUrlCode);
        member.changeBasicProfile(false);
    }

    @Transactional
    public void deletePicture(SessionIdRequest request) {
        Member member = sessionManager.findBySessionId(request.getId());
        if(member.getBasicProfile()) return;

        s3Uploader.deletePicture(member.getUrlCode());
        member.changeUrlCode(EMPTY_STRING);
        member.changeBasicProfile(true);
    }
}
