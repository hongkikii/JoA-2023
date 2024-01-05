package com.mjuAppSW.joA.domain.heart.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class RoomExistedException extends BusinessException {

    public RoomExistedException() {
        super(ErrorCode.ROOM_EXISTED);
    }
}
