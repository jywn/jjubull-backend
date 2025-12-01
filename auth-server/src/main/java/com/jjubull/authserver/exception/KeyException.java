package com.jjubull.authserver.exception;

import com.jjubull.common.exception.ErrorCode;
import com.jjubull.common.exception.SystemException;

public class KeyException extends SystemException {
    public KeyException(Throwable cause) {
        super(ErrorCode.KEY, cause);
    }
}
