package com.jjubull.authserver.exception;

import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;

public class AccessTokenExpiredException extends BusinessException {
    public AccessTokenExpiredException(String message) {
        super(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
