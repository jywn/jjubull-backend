package com.jjubull.authserver.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimiter {

    private final StringRedisTemplate stringRedisTemplate;

    public boolean allow(String key, int limit, Duration ttl) {
        try {
            Long count = stringRedisTemplate.opsForValue().increment(key);
            if (Objects.equals(count, 1L)) {
                stringRedisTemplate.expire(key, ttl);
            }
            return count != null && count <= limit;
        } catch (Exception e) {
            log.error("Redis error occurred(fail-open)", e);
            return true;
        }
    }
}
