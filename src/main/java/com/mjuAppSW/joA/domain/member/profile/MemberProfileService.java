package com.mjuAppSW.joA.domain.member.profile;

import com.mjuAppSW.joA.domain.member.dto.request.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.response.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.request.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SetPageResponse;
import com.mjuAppSW.joA.domain.member.dto.response.StatusResponse;

public interface MemberProfileService {

    SetPageResponse getSettingPage(Long id);

    MyPageResponse getMyPage(Long id);

    StatusResponse transBio(BioRequest request);

    StatusResponse transPicture(PictureRequest request);

    StatusResponse deletePicture(SessionIdRequest request);
}
