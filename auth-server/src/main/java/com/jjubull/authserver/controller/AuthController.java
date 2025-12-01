package com.jjubull.authserver.controller;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.common.domain.Provider;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.service.LoginService;
import com.jjubull.authserver.service.TokenService;
import com.jjubull.authserver.service.OAuth2UserService;
import com.jjubull.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final TokenService tokenService;
    private final OAuth2UserService userService;

    @PostMapping("/token/refresh")
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@CookieValue("refresh_token") String refreshToken) {

        RefreshTokenDto refreshTokenDto = tokenService.deleteRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("로그아웃에 성공하였습니다."));
    }

    @GetMapping("/{provider}/start")
    public ResponseEntity<Void> authStart(@PathVariable String provider) {

        String url = loginService.start(provider);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/{provider}/callback")
    public ResponseEntity<ApiResponse<String>> callback(@RequestParam String code, @PathVariable String provider) {

        String idToken = loginService.callBack(code, provider);

        OAuth2User user = userService.getUserByToken(idToken, provider);

        String myAccessToken = tokenService.buildMyAccessToken(user);

        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("사용자 로그인에 성공하였습니다."));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Void>> admin() {

        OAuth2User user = userService.getUser("01055839181", Provider.LOCAL);
        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());
        String myAccessToken = tokenService.buildMyAccessToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken)
                .body(ApiResponse.success("관리자 로그인에 성공하였습니다."));
    }
}
