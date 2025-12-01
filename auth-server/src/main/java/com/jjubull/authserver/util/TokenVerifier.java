package com.jjubull.authserver.util;

import com.jjubull.authserver.exception.AccessTokenExpiredException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

public class TokenVerifier {

    public static void verifyToken(SignedJWT signedJWT, JWSVerifier verifier) {
        try {
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid Signature");
            }

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new AccessTokenExpiredException("Access Token is expired");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void verifyOpToken(String issuer, String aud, SignedJWT jwt) throws ParseException {
        if (!issuer.equals(jwt.getJWTClaimsSet().getIssuer())) {
            throw new RuntimeException("Invalid Issuer");
        }

        if (!aud.equals(jwt.getJWTClaimsSet().getAudience().getFirst())) {
            throw new RuntimeException("Invalid Client Id");
        }
    }

}
