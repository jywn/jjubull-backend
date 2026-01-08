package com.jjubull.authserver.controller;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.service.AccessTokenService;
import com.jjubull.authserver.service.OAuth2UserService;
import com.jjubull.authserver.store.RefreshTokenStore;
import com.jjubull.authserver.service.RefreshTokenService;
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

    private final RefreshTokenService refreshTokenService;
    private final OAuth2UserService userService;
    private final RefreshTokenStore refreshTokenStore;
    private final AccessTokenService accessTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> exchangeToken(@CookieValue("refresh_token") String refreshToken) {

        Long userId = refreshTokenStore.getUserIdAndDelete(refreshToken);
        RefreshTokenDto refreshTokenDto = refreshTokenService.createRefreshToken(userId);

        OAuth2User user = userService.getUser(userId);
        String accessToken = accessTokenService.buildMyAccessToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("리프레시 토큰을 사용하여 액세스 토큰을 발급하였습니다."));
    }
}
