package com.jjubull.resourceserver.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.common.exception.ErrorCode;
import com.jjubull.resourceserver.common.exception.AccessTokenBlockedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jjubull.common.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {

        ApiResponse<Object> apiResponse = null;
        if (ex instanceof AccessTokenBlockedException) {
            apiResponse = ApiResponse.failure(ACCESS_TOKEN_BLOCKED.getCode(), ACCESS_TOKEN_BLOCKED.getMessage(), null);
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
