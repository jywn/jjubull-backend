package com.jjubull.resourceserver.message.exception;

import com.jjubull.resourceserver.common.exception.ErrorCode;
import com.jjubull.resourceserver.common.exception.SystemException;

public class MessageSendFailedException extends SystemException {

    public MessageSendFailedException(Throwable cause) {
        super(ErrorCode.MESSAGE_SEND_FAILED, cause);
    }
}
