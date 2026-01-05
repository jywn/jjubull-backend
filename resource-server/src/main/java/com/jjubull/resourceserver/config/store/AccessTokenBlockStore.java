package com.jjubull.resourceserver.config.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AccessTokenBlockStore {

    private static final String PREFIX = "bl:at:";
    private final StringRedisTemplate stringRedisTemplate;

    public boolean isBlocked(String jti) {
        String key = PREFIX + jti;
        return stringRedisTemplate.hasKey(key);
    }
}
