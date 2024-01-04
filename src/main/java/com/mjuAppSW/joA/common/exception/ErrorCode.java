package com.mjuAppSW.joA.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(500, "C001", "서버에 오류가 발생하였습니다."),

    // Member
    MEMBER_NOT_EXISTED(404,"M001","사용자를 찾을 수 없습니다."),
    ACCESS_FORBIDDEN(403, "M002", "접근 권한이 없는 계정입니다."),
    S3_INVALID(500, "M003", "이미지 업로드에 실패하였습니다.");

    private final int status;
    private final String code;
    private final String message;
}
