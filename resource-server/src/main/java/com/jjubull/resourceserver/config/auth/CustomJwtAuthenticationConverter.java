package com.jjubull.resourceserver.config.auth;

import com.jjubull.resourceserver.common.AuthenticatedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("GRADE_" + jwt.getClaim("grade").toString()));
        AuthenticatedUser authenticatedUser = AuthenticatedUser.from(jwt);

        return new CustomJwtAuthenticationToken(jwt, authorities, authenticatedUser);
    }
}
