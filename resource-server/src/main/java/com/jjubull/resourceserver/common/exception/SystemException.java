package com.jjubull.resourceserver.common.exception;

public class SystemException extends RuntimeException {

    private final ErrorCode errorCode;

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
