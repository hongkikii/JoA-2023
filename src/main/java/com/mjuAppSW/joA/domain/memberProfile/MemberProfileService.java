package com.mjuAppSW.joA.domain.memberProfile;

import com.mjuAppSW.joA.domain.member.dto.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.NameRequest;
import com.mjuAppSW.joA.domain.member.dto.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.SetResponse;
import com.mjuAppSW.joA.domain.member.dto.StatusResponse;

public interface MemberProfileService {

    SetResponse set(Long id);

    MyPageResponse sendMyPage(Long id);

    StatusResponse transName(NameRequest request);

    StatusResponse transBio(BioRequest request);

    StatusResponse transPicture(PictureRequest request);

    StatusResponse deletePicture(SessionIdRequest request);
}
