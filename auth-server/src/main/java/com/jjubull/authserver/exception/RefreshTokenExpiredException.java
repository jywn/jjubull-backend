package com.jjubull.authserver.exception;

import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;

public class RefreshTokenExpiredException extends BusinessException {
    public RefreshTokenExpiredException(String message) {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
