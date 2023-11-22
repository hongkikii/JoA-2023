package com.mjuAppSW.joA.constant;

public class Constants {

    public static class Heart {
        public static Integer USER_IS_MATCHED = 2;
        public static Integer USER_IS_NAMED = 1;
        public static Integer USER_IS_ANONYMOUS = 0;

        public static Integer MEMBER_IS_INVALID = 2;
        public static Integer HEART_IS_EXISTED = 1;
        public static Integer ROOM_IS_EXISTED = 3;
    }

    public static class Vote {
        public static Integer MEMBER_IS_INVALID = 3;
        public static Integer VOTE_CATEGORY_IS_NOT_VALID = 2;
        public static Integer VOTE_IS_EXISTED = 1;
        public static Integer GIVE_MEMBER_CANNOT_SEND_TO_OPPONENT = 4;
    }

    public static class GeographyUpdate {
        public static Integer MEMBER_IS_STOPPED = 3;
        public static Integer MEMBER_IS_INVALID = 2;
        public static Integer COLLEGE_IS_INVALID = 1;
    }

    public static class GeographyList {
        public static Integer MEMBER_IS_STOPPED = 4;
        public static Integer MEMBER_IS_INVALID = 3;
        public static Integer MEMBER_GEOGRAPHY_IS_INVALID = 2;
        public static Integer MEMBER_IS_OUT_OF_COLLEGE = 1;
    }

    public static class GeographyInfo {
        public static Integer MEMBER_IS_INVALID = 1;
    }

    public static class Member {
        public static Integer COLLEGE_IS_INVALID = 3;
        public static Integer MEMBER_IS_EXISTED = 1;
        public static Integer MAIL_IS_USING = 2;
    }

    public static class Cache {
        public static String CERTIFY_NUMBER = "CertifyNum";
        public static String BEFORE_EMAIL = "BeforeEmail";
        public static String AFTER_EMAIL = "AfterEmail";
        public static String ID = "Id";

        public static Integer BEFORE_CERTIFY_TIME = 7;
        public static Integer AFTER_CERTIFY_TIME = 60;
        public static Integer AFTER_SAVE_LOGIN_ID_TIME = 30;
    }

    public static class MAIL {
        public static String USER_ID_IS = "id";
        public static String TEMPORARY_PASSWORD_IS = "임시 비밀번호";
        public static String CERTIFY_NUMBER_IS = "인증번호";
    }

    public static class Auth {
        public static Integer CERTIFY_NUMBER_IS_INVALID = 1;
        public static Integer LOGIN_ID_IS_INVALID = 4;
        public static Integer LOGIN_ID_IS_EXISTED = 1;
        public static Integer LOGIN_ID_IS_CACHED = 2;
        public static Integer COLLEGE_IS_NOT_EXISTED = 3;
        public static Integer MAIL_IS_NOT_CACHED = 2;
        public static Integer SESSION_ID_IS_NOT_EXISTED = 3;
    }

    public static class Join {
        public static Integer SESSION_ID_IS_NOT_EXISTED = 1;
        public static Integer LOGIN_ID_IS_NOT_CACHED = 2;
        public static Integer COLLEGE_IS_INVALID = 3;
        public static Integer PASSWORD_IS_INVALID = 4;
        public static Integer MAIL_IS_FORBIDDEN = 5;
    }

    public static class Login {
        public static Integer LOGIN_ID_IS_NOT_EXISTED = 1;
        public static Integer PASSWORD_IS_NOT_EXISTED = 2;
    }

    public static class Logout {
        public static Integer MEMBER_IS_NOT_EXISTED = 1;
    }

    public static class FindId {
        public static Integer COLLEGE_IS_NOT_EXISTED = 1;
        public static Integer MEMBER_IS_NOT_EXISTED = 2;
    }

    public static class TransPassword {
        public static Integer PASSWORD_IS_NOT_EQUAL = 1;
        public static Integer MEMBER_IS_NOT_EXISTED = 2;
        public static Integer PASSWORD_IS_INVALID = 3;
    }

    public static class Withdrawal {
        public static Integer MEMBER_IS_NOT_EXISTED = 2;
    }

    public static class Set {
        public static Integer MEMBER_IS_NOT_EXISTED = 1;
    }

    public static class TransPicture {
        public static Integer MEMBER_IS_NOT_EXISTED = 2;
    }

    public static class ReportVote {
        public static Integer VOTE_IS_NOT_EXISTED = 2;
        public static Integer REPORT_CATEGORY_IS_NOT_EXISTED = 3;
        public static Integer EQUAL_VOTE_REPORT_IS_EXISTED = 1;
    }

    public static class S3Uploader {
        public static String ERROR = "error";
        public static Integer S3_UPLOAD_IS_INVALID = 1;
        public static Integer S3_DELETE_IS_INVALID = 1;

    }
    public static Integer NORMAL_OPERATION = 0;
    public static String EMPTY_STRING = "";
    public static String EMAIL_SPLIT = "@";
}
