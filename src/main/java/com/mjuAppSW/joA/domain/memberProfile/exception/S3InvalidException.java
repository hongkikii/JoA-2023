package com.mjuAppSW.joA.domain.memberProfile.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class S3InvalidException extends BusinessException {

    public S3InvalidException() {
        super(ErrorCode.S3_INVALID);
    }
}
