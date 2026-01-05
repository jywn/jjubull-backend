package com.jjubull.authserver.store;

import com.jjubull.authserver.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore {

    private static final String PREFIX = "rt:";
    private final StringRedisTemplate stringRedisTemplate;

    public void save(String refreshToken, Long userId, Duration ttl) {
        String key = PREFIX + refreshToken;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(userId), ttl);
    }

    public Long findUserId(String refreshToken) {
        String key = PREFIX + refreshToken;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return Long.parseLong(value);
    }

    public void delete(String refreshToken) {
        String key = PREFIX + refreshToken;
        stringRedisTemplate.delete(key);
    }

    public Long getUserIdAndDelete(String refreshToken) {
        String key = PREFIX + refreshToken;
        String userIdStr = stringRedisTemplate.opsForValue().getAndDelete(key);
        if (userIdStr == null) throw new RefreshTokenNotFoundException();
        return Long.parseLong(userIdStr);
    }
}
