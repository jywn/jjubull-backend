package com.jjubull.authserver.service;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.store.RefreshTokenStore;
import com.jjubull.authserver.util.CookieBuilder;
import com.jjubull.authserver.util.JwkUtil;
import com.jjubull.authserver.util.JwtBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private static final Long RT_MAX_SECONDS = 14L * 24 * 60 * 60;
    private static final Duration RT_MAX_DAYS = Duration.ofDays(14);
    private final RefreshTokenStore refreshTokenStore;
    private final JwkUtil jwkUtil;

    public String buildMyAccessToken(OAuth2User user) {
        try {
            return JwtBuilder.buildMyToken(jwkUtil.getMyRsaKey(), user.getSub(), user.getProvider().toString(), user.getGrade().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RefreshTokenDto createRefreshToken(Long userId) {

        String refreshToken = UUID.randomUUID().toString();
        refreshTokenStore.save(refreshToken, userId, RT_MAX_DAYS);
        ResponseCookie cookie = CookieBuilder.buildCookie("refresh_token", refreshToken, RT_MAX_SECONDS);

        return RefreshTokenDto.builder()
                .refreshToken(refreshToken)
                .cookie(cookie)
                .build();
    }

    public RefreshTokenDto deleteRefreshToken(String token) {

        refreshTokenStore.delete(token);
        ResponseCookie cookie = CookieBuilder.buildCookie("refresh_token", "", 0L);

        return RefreshTokenDto.builder()
                .refreshToken("")
                .cookie(cookie)
                .build();
    }
}
