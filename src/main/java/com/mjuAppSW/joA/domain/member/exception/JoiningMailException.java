package com.mjuAppSW.joA.domain.member.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class UsingMailException extends BusinessException {

    public UsingMailException() {
        super(ErrorCode.MAIL_ALREADY_USED);
    }
}
