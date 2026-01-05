package com.jjubull.resourceserver.common;

import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.common.exception.AccessTokenExpiredException;
import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> businessExceptionHandler(BusinessException e) {

        log.warn("Business error occurred: code={}, message={}", e.getCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(e.getMessage(), null));

    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<Void>> systemExceptionHandler(SystemException e) {

        log.error("System error occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(e.getMessage(), null));
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> accessTokenExpiredExceptionHandler(AccessTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(e.getMessage(), null));
    }
}
