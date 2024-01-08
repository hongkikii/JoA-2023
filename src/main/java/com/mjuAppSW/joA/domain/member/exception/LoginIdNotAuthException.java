package com.mjuAppSW.joA.domain.member.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class LoginIdNotAuthException extends BusinessException {

    public LoginIdNotAuthException() {
        super(ErrorCode.LOGIN_ID_NOT_AUTH);
    }
}
