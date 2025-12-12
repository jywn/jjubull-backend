package com.jjubull.resourceserver.schedule.exception;

import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;

public class NoPossibleSeatException extends BusinessException {
    public NoPossibleSeatException() {
        super(ErrorCode.NO_POSSIBLE_SEAT);
    }
}
