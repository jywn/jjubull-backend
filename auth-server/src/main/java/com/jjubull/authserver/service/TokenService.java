package com.jjubull.authserver.service;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.domain.RefreshToken;
import com.jjubull.authserver.dto.RefreshTokenDto;
import com.jjubull.authserver.repository.RefreshTokenRepository;
import com.jjubull.authserver.util.CookieBuilder;
import com.jjubull.authserver.util.JwkUtil;
import com.jjubull.authserver.util.JwtBuilder;
import com.jjubull.authserver.util.TokenVerifier;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWKSource<SecurityContext> jwkSource;
    private final Long refreshTokenMaxAge = 60L * 60 * 24 * 14;
    private final JwkUtil jwkUtil;

    public String buildMyAccessToken(OAuth2User user) {
        try {
            return JwtBuilder.buildMyToken(jwkUtil.getMyRsaKey(), user.getSub(), user.getProvider().toString(), user.getGrade().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RefreshTokenDto createRefreshToken(Long userId) {

        String token = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(60L * 60 * 24 * 14);

        RefreshToken refreshToken = RefreshToken.create(userId, token, expiresAt);
        ResponseCookie cookie = CookieBuilder.buildCookie("refresh_token", refreshToken.getValue(), refreshTokenMaxAge);

        refreshTokenRepository.save(refreshToken);

        return RefreshTokenDto.builder()
                .refreshToken(refreshToken.getValue())
                .cookie(cookie)
                .build();
    }

    public Long verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByValue(token)
                .orElseThrow(EntityNotFoundException::new);

        if (Instant.now().isAfter(refreshToken.getExpiresAt())) {
            throw new RuntimeException("Expired Token");
        }

        return refreshToken.getUserId();
    }

    public RefreshTokenDto deleteRefreshToken(String token) {

        refreshTokenRepository.deleteByValue(token);

        ResponseCookie cookie = CookieBuilder.buildCookie("refresh_token", "", 0L);

        return RefreshTokenDto.builder()
                .refreshToken("")
                .cookie(cookie)
                .build();
    }

}
