package com.jjubull.authserver.service;

import com.jjubull.common.domain.User;
import com.jjubull.authserver.dto.ValidatedMyTokenDto;
import com.jjubull.authserver.exception.NewUserException;
import com.jjubull.common.domain.Grade;
import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.common.domain.Provider;
import com.jjubull.authserver.repository.OAuth2UserRepository;
import com.jjubull.authserver.repository.UserRepository;
import com.jjubull.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService {

    private final OAuth2UserRepository oAuth2UserRepository;
    private final UserRepository userRepository;
    private final TokenValidator tokenValidator;

    public OAuth2User getUser(String sub, Provider provider) {
        return oAuth2UserRepository.findOAuth2UserByProviderAndSub(provider, sub)
                .orElseThrow(() -> new NewUserException(provider, sub));
    }

    public OAuth2User getUser(Long id) {
        return oAuth2UserRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public OAuth2User getUserByToken(String token, String provider) {

        String sub = tokenValidator.validateOpToken(token, provider);

        return oAuth2UserRepository.findOAuth2UserByProviderAndSub(Provider.from(provider), sub)
                .orElseThrow(() -> new NewUserException(Provider.from(provider), sub));
    }

    public OAuth2User newUser(String jwt, String username, String nickname, String phone) {

        ValidatedMyTokenDto dto = tokenValidator.validateMyToken(jwt);
        Provider provider = dto.getProvider();
        String sub = dto.getSub();

        if (oAuth2UserRepository.existsByProviderAndSub(provider, sub)) {
                throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        userRepository.save(User.create(username, nickname, Grade.BASIC, phone, sub, provider));
        return oAuth2UserRepository.save(OAuth2User.create(provider, sub, username));
    }
}
