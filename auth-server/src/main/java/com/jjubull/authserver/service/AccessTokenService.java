package com.jjubull.authserver.service;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.store.AccessTokenBlockStore;
import com.jjubull.authserver.util.JwkUtil;
import com.jjubull.authserver.util.JwtBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwkUtil jwkUtil;
    private final AccessTokenBlockStore accessTokenBlockStore;

    public String buildMyAccessToken(OAuth2User user) {
        return JwtBuilder.buildMyToken(jwkUtil.getMyRsaKey(), user.getSub(), user.getProvider().toString(), user.getGrade().toString());
    }

    public void blockMyAccessToken(Jwt accessToken) {
        accessTokenBlockStore.block(accessToken.getId(),
                Duration.between(Instant.now(), accessToken.getExpiresAt()));
    }
}
