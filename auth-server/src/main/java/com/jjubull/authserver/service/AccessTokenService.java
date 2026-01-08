package com.jjubull.authserver.service;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.authserver.util.JwkUtil;
import com.jjubull.authserver.util.JwtBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwkUtil jwkUtil;

    public String buildMyAccessToken(OAuth2User user) {
        return JwtBuilder.buildMyToken(jwkUtil.getMyRsaKey(), user.getSub(), user.getProvider().toString(), user.getGrade().toString());
    }
}
