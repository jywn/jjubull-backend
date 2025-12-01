package com.jjubull.authserver.util;

import org.springframework.http.ResponseCookie;

// 배포 수정
public class CookieBuilder {

    public static ResponseCookie buildCookie(String cookieName, Object cookieValue, Long maxAge) {
        return ResponseCookie.from(cookieName, cookieValue.toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
