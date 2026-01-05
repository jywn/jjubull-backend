package com.jjubull.authserver.exception;

import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
