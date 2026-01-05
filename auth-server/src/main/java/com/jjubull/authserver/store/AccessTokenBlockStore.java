package com.jjubull.authserver.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AccessTokenBlockStore {

    private static final String PREFIX = "bl:at:";
    private final StringRedisTemplate stringRedisTemplate;

    public void block(String jti, Duration ttl) {
        if (!ttl.isPositive()) return;

        String key = PREFIX + jti;
        stringRedisTemplate.opsForValue().set(key, "1", ttl);
    }

    public boolean isBlocked(String jti) {
        String key = PREFIX + jti;
        return stringRedisTemplate.hasKey(key);
    }
}
