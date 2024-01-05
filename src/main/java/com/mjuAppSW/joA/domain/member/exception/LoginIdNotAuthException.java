package com.mjuAppSW.joA.domain.member.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class LoginIdNotFoundException extends BusinessException {

    public LoginIdNotFoundException() {
        super(ErrorCode.LOGIN_ID_NOT_FOUND);
    }
}
