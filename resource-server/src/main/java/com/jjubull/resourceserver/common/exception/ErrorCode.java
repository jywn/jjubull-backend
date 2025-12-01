package com.jjubull.resourceserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /** SMS 예외 **/
    MESSAGE_SEND_FAILED("M001", "메시지 전송 API 호출 실패"),

    /** User 예외 **/
    USER_NOT_FOUND("U001", "사용자 조회 실패");

    private final String code;
    private final String message;

}
