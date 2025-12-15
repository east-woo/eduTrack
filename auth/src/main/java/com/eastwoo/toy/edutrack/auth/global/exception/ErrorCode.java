package com.eastwoo.toy.edutrack.auth.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 공통 */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "C001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C002", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C003", "권한이 없습니다."),

    /* Auth */
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A001", "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "A002", "비밀번호가 올바르지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "A003", "이미 존재하는 이메일입니다."),

    /* 초대(INVITE) */
    INVITE_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "I001", "유효하지 않은 초대 토큰입니다."),
    INVITE_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "I002", "초대 토큰이 만료되었습니다."),
    INVITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "I003", "이미 유효한 초대 토큰이 존재합니다."),

    /* 강사(Instructor) */
    SIGNUP_REQUEST_EXISTS(HttpStatus.CONFLICT, "T001", "이미 강사 가입 요청이 접수되었습니다."),
    ALREADY_TEACHER(HttpStatus.CONFLICT, "T002", "이미 강사입니다."),
    PROMOTION_REQUEST_EXISTS(HttpStatus.CONFLICT, "T003", "이미 승급 요청이 접수되었습니다."),
    SIGNUP_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "T004", "강사 가입 요청이 존재하지 않습니다."),
    PROMOTION_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "T005", "강사 승급 요청이 존재하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
    }