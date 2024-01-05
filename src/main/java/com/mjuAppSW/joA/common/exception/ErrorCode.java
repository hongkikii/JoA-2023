package com.mjuAppSW.joA.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(500, "C001", "서버에 오류가 발생하였습니다."),

    // Member
    MEMBER_NOT_FOUND(404,"M001","사용자를 찾을 수 없습니다."),
    ACCESS_FORBIDDEN(403, "M002", "접근 권한이 없는 계정입니다."),
    S3_INVALID(500, "M003", "이미지 업로드에 실패하였습니다."),

    // Heart
    HEART_ALREADY_EXISTED(409, "H001", "이미 하트가 존재합니다."),

    // Vote
    VOTE_NOT_FOUND(404, "V001", "투표가 존재하지 않습니다."),
    VOTE_CATEGORY_NOT_FOUND(404, "V002", "투표 카테고리가 존재하지 않습니다."),
    VOTE_ALREADY_EXISTED(409, "V003", "이미 투표가 존재합니다."),

    // Vote Report
    VOTE_REPORT_ALREADY_EXISTED(409, "VR001", "이미 투표 신고가 존재합니다."),

    // Report Category
    REPORT_CATEGORY_NOT_FOUND(404, "RC001", "신고 카테고리가 존재하지 않습니다."),

    // Block
    BLOCK_EXISTED(403, "B001", "차단 조치가 이루어진 계정입니다."),

    // Room
    ROOM_EXISTED(409, "R001", "이미 채팅방이 존재합니다.");

    private final int status;
    private final String code;
    private final String message;
}
