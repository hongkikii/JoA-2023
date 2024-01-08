package com.mjuAppSW.joA.domain.member.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class MailNotCachedException extends BusinessException {

    public MailNotCachedException() {
        super(ErrorCode.MAIL_NOT_CACHED);
    }
}
