package com.jjubull.resourceserver.config;

import com.jjubull.resourceserver.common.AuthenticatedUser;
import com.jjubull.resourceserver.config.auth.CustomJwtAuthenticationConverter;
import com.jjubull.resourceserver.config.auth.CustomJwtAuthenticationToken;
import com.jjubull.resourceserver.config.auth.JsonAuthenticationEntryPoint;
import com.jjubull.resourceserver.config.filter.AccessTokenBlacklistFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.ArrayList;

@Configuration
public class DefaultSecurityConfig {
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          AccessTokenBlacklistFilter accessTokenBlacklistFilter,
                                                          CustomJwtAuthenticationConverter jwtAuthenticationConverter,
                                                          JsonAuthenticationEntryPoint entryPoint) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
//                        .anyRequest().permitAll()) // 부하테스트 용도
                        .requestMatchers("/schedules/*/reservation/normal", "/schedules/main", "/schedules/main/rdb").permitAll()
                        .requestMatchers(HttpMethod.POST, "/schedules/*/reservation").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(accessTokenBlacklistFilter, ExceptionTranslationFilter.class)
                .oauth2ResourceServer(resource -> resource.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                ))
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint))
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
