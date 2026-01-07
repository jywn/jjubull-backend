package com.jjubull.resourceserver.schedule.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ScheduleStore {

    private static final Duration LOCK_TTL = Duration.ofSeconds(3);
    private static final Duration TTL = Duration.ofSeconds(60); // 메인 화면은 짧게

    private final StringRedisTemplate stringRedisTemplate;

    public String readCache(String cacheKey) {
        return stringRedisTemplate.opsForValue().get(cacheKey);
    }

    public boolean getLock(String lockKey, String requestId) {
        try {
            return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, LOCK_TTL));
        } catch (Exception e) {
            return false;
        }
    }

    public void writeCache(String cacheKey, String value) {
        stringRedisTemplate.opsForValue().set(cacheKey, value, TTL);
    }

    public void unlock(String lockKey, String requestId) {
        stringRedisTemplate.delete(lockKey);
    }

    public void hit() {
        Long count = stringRedisTemplate.opsForValue().increment("hit");
        if (Objects.equals(count, 1L)) {
            stringRedisTemplate.expire("hit", Duration.ofSeconds(60));
        }
    }
}
