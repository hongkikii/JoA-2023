package com.mjuAppSW.joA.domain.roomInMember.exception;

import com.mjuAppSW.joA.common.exception.BusinessException;
import com.mjuAppSW.joA.common.exception.ErrorCode;

public class AlreadyExistedRoomInMemberException extends BusinessException {
	public AlreadyExistedRoomInMemberException() { super(ErrorCode.ALREADY_EXISTED_RIM);}
}
