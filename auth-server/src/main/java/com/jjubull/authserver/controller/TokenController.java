package com.jjubull.authserver.controller;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.service.LoginService;
import com.jjubull.authserver.service.OAuth2UserService;
import com.jjubull.authserver.service.TokenService;
import com.jjubull.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final OAuth2UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(@CookieValue("refresh_token") String refreshToken) {

        Long userId = tokenService.verifyRefreshToken(refreshToken);
        OAuth2User user = userService.getUser(userId);
        tokenService.deleteRefreshToken(refreshToken);

        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());
        String accessToken = tokenService.buildMyAccessToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("리프레시 토큰을 사용하여 액세스 토큰을 발급하였습니다."));
    }
}
