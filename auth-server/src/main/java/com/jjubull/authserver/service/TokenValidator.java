package com.jjubull.authserver.service;


import com.jjubull.common.domain.Provider;
import com.jjubull.authserver.dto.ValidatedMyTokenDto;
import com.jjubull.authserver.exception.KeyException;
import com.jjubull.authserver.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TokenValidator {

        private final JwtUtil jwtUtil;
        private final ClientRegistrationRepository clientRegistrationRepository;

        public String validateOpToken(String token, String provider) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);

                RSAKey rsaKey = jwtUtil.getOpRsaKey(
                        clientRegistrationRepository.findByRegistrationId(provider).getProviderDetails().getJwkSetUri(),
                        signedJWT.getHeader().getKeyID()
                );

                jwtUtil.verifyToken(signedJWT, new RSASSAVerifier(rsaKey.toRSAPublicKey()));

                ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);
                jwtUtil.verifyOpToken(clientRegistration.getProviderDetails().getIssuerUri(), clientRegistration.getClientId(), signedJWT);

                return signedJWT.getJWTClaimsSet().getSubject();
            } catch (ParseException | JOSEException | IOException e) {
                throw new KeyException(e);
            }
        }

        public ValidatedMyTokenDto validateMyToken(String jwt) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(jwt);
                RSAKey rsaKey = jwtUtil.getMyRsaKey(signedJWT.getHeader().getKeyID());
                jwtUtil.verifyToken(signedJWT, new RSASSAVerifier(rsaKey.toRSAPublicKey()));

                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

                return ValidatedMyTokenDto.create(Provider.from((String) jwtClaimsSet.getClaim("provider")),
                        jwtClaimsSet.getSubject());

            } catch (ParseException | JOSEException e) {
                throw new KeyException(e);
            }
        }
    }
