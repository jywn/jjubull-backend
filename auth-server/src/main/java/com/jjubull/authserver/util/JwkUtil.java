package com.jjubull.authserver.util;

import com.jjubull.authserver.exception.KeyException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class JwkUtil {

    private final JWKSource<SecurityContext> jwkSource;

    public RSAKey getMyRsaKey(String kid) {
        JWKSelector selector = new JWKSelector(new JWKMatcher.Builder().keyID(kid).build());
        try {
            JWK jwk = jwkSource.get(selector, null).getFirst();
            return jwk.toRSAKey();
        } catch (KeySourceException e) {
            throw new KeyException(e);
        }
    }

    public RSAKey getMyRsaKey() {
        JWKSelector selector = new JWKSelector(new JWKMatcher.Builder().build());
        try {
            JWK jwk = jwkSource.get(selector, null).getFirst();
            return jwk.toRSAKey();
        } catch (KeySourceException e) {
            throw new KeyException(e);
        }
    }

    public RSAKey getOpRsaKey(String keyUrl, String kid) throws IOException, ParseException {
        JWKSet jwkSet = JWKSet.load(new URL(keyUrl));
        JWK jwk = jwkSet.getKeyByKeyId(kid);
        return jwk.toRSAKey();
    }
}
