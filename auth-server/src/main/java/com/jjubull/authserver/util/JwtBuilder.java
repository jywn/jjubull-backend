package com.jjubull.authserver.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

public class JwtBuilder {

    public static String buildMyToken(RSAKey rsaKey, String sub, String provider, String grade) {
        try {
            JWTClaimsSet claims = buildBody(sub, provider, grade);

            JWSHeader header = buildHeader(rsaKey);

            SignedJWT jwt = buildSign(rsaKey, header, claims);

            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SignedJWT buildSign(RSAKey rsaKey, JWSHeader header, JWTClaimsSet claims) throws JOSEException {
        RSASSASigner signer = new RSASSASigner(rsaKey);
        SignedJWT jwt = new SignedJWT(header, claims);
        jwt.sign(signer);
        return jwt;
    }

    private static JWSHeader buildHeader(RSAKey rsaKey) {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.getKeyID())
                .build();
    }

    private static JWTClaimsSet buildBody(String sub, String provider, String grade) {
        return new JWTClaimsSet.Builder()
                .subject(sub)
                .issuer("jjubul-auth-server")
                .audience("jjubul-api-server")
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 10 * 60 * 1000))
                .claim("provider", provider)
                .claim("grade", grade)
                .build();
    }
}
