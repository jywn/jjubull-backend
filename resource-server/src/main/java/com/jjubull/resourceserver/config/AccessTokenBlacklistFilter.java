package com.jjubull.resourceserver.config;

import com.jjubull.common.exception.AccessTokenExpiredException;
import com.jjubull.resourceserver.config.store.AccessTokenBlockStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessTokenBlacklistFilter extends OncePerRequestFilter {

    private final AccessTokenBlockStore accessTokenBlockStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof CustomJwtAuthenticationToken customJwtAuthenticationToken) {
            String jti = customJwtAuthenticationToken.getJwt().getId();
            if (accessTokenBlockStore.isBlocked(jti)) {
                throw new AccessTokenExpiredException();
            }
        }

        filterChain.doFilter(request, response);
    }
}
