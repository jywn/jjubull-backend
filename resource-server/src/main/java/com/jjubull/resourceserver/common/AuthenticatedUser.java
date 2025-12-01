package com.jjubull.resourceserver.common;

import com.jjubull.common.domain.Grade;
import com.jjubull.common.domain.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
@NoArgsConstructor
public class AuthenticatedUser {

    Provider provider;
    String sub;
    Grade grade;

    private AuthenticatedUser(Provider provider, String sub, Grade grade) {
        this.provider = provider;
        this.sub = sub;
        this.grade = grade;
    }

    public static AuthenticatedUser from(Jwt jwt) {
        Provider provider = Provider.from(jwt.getClaimAsString("provider"));
        String sub = jwt.getClaimAsString("sub");
        Grade grade = Grade.from(jwt.getClaimAsString("grade"));

        return new AuthenticatedUser(provider, sub, grade);
    }
}
