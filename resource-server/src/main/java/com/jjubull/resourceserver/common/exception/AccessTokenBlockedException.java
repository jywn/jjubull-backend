package com.jjubull.resourceserver.common.exception;

import com.jjubull.common.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class AccessTokenBlockedException extends AuthenticationException {
    public AccessTokenBlockedException() {
        super(ErrorCode.ACCESS_TOKEN_BLOCKED.getMessage());
    }
}
