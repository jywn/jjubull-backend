package com.jjubull.authserver.test;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @GetMapping("/rdb")
    public String rdb() {
        return refreshTokenRepository.findAll().toString();
    }

    @GetMapping("/redis")
    public Boolean redis() {
        return stringRedisTemplate.hasKey("test:redis");
    }
}
