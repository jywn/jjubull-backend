package com.jjubull.authserver.exception;

import com.jjubull.authserver.util.JwkUtil;
import com.jjubull.authserver.util.JwtBuilder;
import com.jjubull.authserver.util.TokenVerifier;
import com.jjubull.common.domain.Grade;
import com.jjubull.common.domain.Provider;
import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.common.exception.BusinessException;
import com.jjubull.common.exception.SystemException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final JwkUtil jwkUtil;
    private final JWKSource<SecurityContext> jwkSource;

    @ExceptionHandler(NewUserException.class)
    public ResponseEntity<ApiResponse<String>> handleNewUserException(NewUserException e) {

        String jwt = JwtBuilder.buildMyToken(
                jwkUtil.getMyRsaKey(), e.getSub(), e.getProvider().toString(), Grade.GUEST.getValue());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("회원가입이 필요합니다.", jwt));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiResponse<String>> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(e.getMessage(), null));
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessTokenExpiredException(AccessTokenExpiredException e) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(e.getMessage(), null));
    }

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
}
