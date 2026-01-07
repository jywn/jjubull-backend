package com.jjubull.resourceserver.config.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenBlockStore {

    private static final String PREFIX = "bl:at:";
    private final StringRedisTemplate stringRedisTemplate;

    public boolean isBlocked(String jti) {
        String key = PREFIX + jti;
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis error occurred", e);
            return false;
        }
    }
}
