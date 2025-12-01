package com.jjubull.resourceserver.user.exception;

import com.jjubull.resourceserver.common.exception.BusinessException;
import com.jjubull.resourceserver.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
