package com.jjubull.authserver.exception;

import com.jjubull.common.domain.Provider;
import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NewUserException extends BusinessException {

    private final Provider provider;
    private final String sub;

    public NewUserException(Provider provider, String sub) {
        super(ErrorCode.NEW_USER);
        this.provider = provider;
        this.sub = sub;
    }
}
