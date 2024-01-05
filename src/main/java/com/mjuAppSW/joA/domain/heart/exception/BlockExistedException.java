package com.mjuAppSW.joA.domain.heart.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class BlockExistedException extends BusinessException {

    public BlockExistedException() {
        super(ErrorCode.BLOCK_EXISTED);
    }
}
