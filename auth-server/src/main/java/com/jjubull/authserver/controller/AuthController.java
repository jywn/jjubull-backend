package com.jjubull.authserver.controller;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.service.AccessTokenService;
import com.jjubull.authserver.service.LoginService;
import com.jjubull.authserver.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final OAuth2UserService userService;

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

        String myAccessToken = accessTokenService.buildMyAccessToken(user);

        RefreshTokenDto refreshTokenDto = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("사용자 로그인에 성공하였습니다."));
    }
}
