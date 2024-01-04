package com.mjuAppSW.joA.domain.member.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class MemberNotExistedException extends BusinessException {

    public MemberNotExistedException() {
        super(ErrorCode.MEMBER_NOT_EXISTED);
    }
}
