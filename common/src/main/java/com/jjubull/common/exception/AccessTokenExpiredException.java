package com.jjubull.common.exception;

public class AccessTokenExpiredException extends BusinessException {
    public AccessTokenExpiredException() {
        super(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
