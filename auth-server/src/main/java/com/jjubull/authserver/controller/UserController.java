package com.jjubull.authserver.controller;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.dto.NewUserDto;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.service.AccessTokenService;
import com.jjubull.authserver.store.AccessTokenBlockStore;
import com.jjubull.authserver.service.OAuth2UserService;
import com.jjubull.authserver.service.RefreshTokenService;
import com.jjubull.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuth2UserService oAuth2UserService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody NewUserDto dto) {

        OAuth2User user = oAuth2UserService.newUser(dto.getJwt(), dto.getUsername(), dto.getNickname(), dto.getPhone());
        String myAccessToken = accessTokenService.buildMyAccessToken(user);
        RefreshTokenDto refreshTokenDto = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("회원가입을 성공하였습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @CookieValue("refresh_token") String refreshToken,
            @AuthenticationPrincipal Jwt accessToken) {

        RefreshTokenDto refreshTokenDto = refreshTokenService.deleteRefreshToken(refreshToken);
        accessTokenService.blockMyAccessToken(accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("로그아웃에 성공하였습니다."));
    }
}
