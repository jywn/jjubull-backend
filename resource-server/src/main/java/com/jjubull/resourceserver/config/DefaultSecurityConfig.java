package com.jjubull.resourceserver.config;

import com.jjubull.resourceserver.common.AuthenticatedUser;
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
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.ArrayList;

@Configuration
public class DefaultSecurityConfig {
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          AccessTokenBlacklistFilter accessTokenBlacklistFilter,
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
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint))
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {

        return new Converter<Jwt, AbstractAuthenticationToken>() {

            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {

                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("GRADE_" + jwt.getClaim("grade").toString()));

                AuthenticatedUser authenticatedUser = AuthenticatedUser.from(jwt);

                return new CustomJwtAuthenticationToken(jwt, authorities, authenticatedUser);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
