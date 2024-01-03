package com.mjuAppSW.joA.domain.member.authentication;


import com.mjuAppSW.joA.domain.member.dto.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.IdRequest;
import com.mjuAppSW.joA.domain.member.dto.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginResponse;
import com.mjuAppSW.joA.domain.member.dto.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.StatusResponse;
import com.mjuAppSW.joA.domain.member.dto.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailResponse;
import com.mjuAppSW.joA.domain.member.dto.UNumRequest;

public interface AuthenticationService {

    UMailResponse sendCertifyNum(UMailRequest request);

    StatusResponse authCertifyNum(UNumRequest request);

    StatusResponse verifyId(Long id, String loginId);

    StatusResponse join(JoinRequest request);

    LoginResponse login(LoginRequest request);

    StatusResponse logout(IdRequest request);

    StatusResponse findId(FindIdRequest request);

    StatusResponse findPassword(FindPasswordRequest request);

    StatusResponse transPassword(TransPasswordRequest request);

    StatusResponse withdrawal(SessionIdRequest request);

}
