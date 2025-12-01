package com.jjubull.resourceserver.user.service;

import com.jjubull.common.domain.Provider;
import com.jjubull.common.domain.User;
import com.jjubull.resourceserver.user.dto.command.MyProfileDto;
import com.jjubull.common.exception.UserNotFoundException;
import com.jjubull.resourceserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public MyProfileDto getMyProfile(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return MyProfileDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .grade(user.getGrade())
                .phone(user.getPhone())
                .build();
    }

    public User findUser(Provider provider, String sub) {
        return userRepository.findByProviderAndSub(provider, sub).orElseThrow(UserNotFoundException::new);
    }

}
