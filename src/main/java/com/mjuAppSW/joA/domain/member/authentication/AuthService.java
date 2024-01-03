package com.mjuAppSW.joA.domain.member.authentication;


import com.mjuAppSW.joA.domain.member.dto.request.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.request.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.request.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.response.LoginResponse;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.StatusResponse;
import com.mjuAppSW.joA.domain.member.dto.request.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SendCertifyNumRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SendCertifyNumResponse;
import com.mjuAppSW.joA.domain.member.dto.request.VerifyCertifyNumRequest;

public interface AuthService {

    SendCertifyNumResponse sendCertifyNum(SendCertifyNumRequest request);

    StatusResponse verifyCertifyNum(VerifyCertifyNumRequest request);

    StatusResponse verifyId(Long id, String loginId);

    StatusResponse join(JoinRequest request);

    LoginResponse login(LoginRequest request);

    StatusResponse logout(SessionIdRequest request);

    StatusResponse findId(FindIdRequest request);

    StatusResponse findPassword(FindPasswordRequest request);

    StatusResponse transPassword(TransPasswordRequest request);

    StatusResponse withdrawal(SessionIdRequest request);

}
