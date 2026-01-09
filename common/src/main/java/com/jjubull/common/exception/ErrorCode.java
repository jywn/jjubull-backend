package com.jjubull.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /** SMS 예외 **/
    MESSAGE_SEND_FAILED("M001", "메시지 전송 API 호출 실패"),

    /** User 예외 **/
    USER_NOT_FOUND("U001", "사용자 조회 실패"),
    USER_DUPLICATED("U002", "사용자 정보 중복"),
    NEW_USER("U003", "사용자 가입 필요"),

    /** Refresh Token 예외 **/
    REFRESH_TOKEN_NOT_FOUND("R001", "리프레시 토큰 조회 실패"),
    REFRESH_TOKEN_EXPIRED("R002", "리프레시 토큰 만료"),

    /** Access token 예외 **/
    ACCESS_TOKEN_EXPIRED("A002", "액세스 토큰 만료"),
    ACCESS_TOKEN_BLOCKED("A003", "액세스 토큰 차단됨"),

    /** Key 예외 **/
    KEY("K001", "잘못된 키 사용"),

    /** Schedule 예외 **/
    NO_POSSIBLE_SEAT("S001", "잔여 좌석이 없습니다.");

    private final String code;
    private final String message;

}
