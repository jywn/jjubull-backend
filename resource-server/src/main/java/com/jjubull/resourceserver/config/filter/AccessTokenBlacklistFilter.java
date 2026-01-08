package com.jjubull.resourceserver.config.filter;

import com.jjubull.resourceserver.config.auth.CustomJwtAuthenticationToken;
import com.jjubull.resourceserver.store.AccessTokenBlockStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                throw new AuthenticationServiceException("Access token blocked: " + jti);
            }
        }

        filterChain.doFilter(request, response);
    }
}
