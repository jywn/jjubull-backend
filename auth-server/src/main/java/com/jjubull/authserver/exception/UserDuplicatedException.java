package com.jjubull.authserver.exception;

import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;

public class UserDuplicatedException extends BusinessException {
    public UserDuplicatedException() {
        super(ErrorCode.USER_DUPLICATED);
    }
}
