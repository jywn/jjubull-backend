package com.jjubull.resourceserver.schedule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.UUID.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final Duration WAIT_BACKOFF = Duration.ofMillis(50);
    private static final TypeReference<List<ScheduleMainDto>> LIST_TYPE = new TypeReference<>() {};
    private final ObjectMapper objectMapper;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleStore scheduleStore;

    public List<ScheduleMainDto> getMainSchedules(LocalDateTime from, LocalDateTime to) {
        if (!isForMain(from, to)) {
            return scheduleRepository.getSchedules(from, to);
        }

        String yearMonthKey = YearMonth.from(from).toString();
        String cacheKey = "cache:main:" + yearMonthKey;
        String lockKey  = "lock:main:" + yearMonthKey;
        String requestId = randomUUID().toString();

        List<ScheduleMainDto> cachedSchedules = tryReadCache(cacheKey);
        if (cachedSchedules != null) return cachedSchedules;

        boolean locked = scheduleStore.getLock(lockKey, requestId);
        if (!locked) {
            cachedSchedules = tryReadCacheAgain(cacheKey);
            if (cachedSchedules != null) return cachedSchedules;
        }

        List<ScheduleMainDto> schedules = scheduleRepository.getSchedules(from, to);

        if (locked) tryWriteCache(cacheKey, lockKey, requestId, schedules);

        return schedules;
    }

    public List<ScheduleMainDto> tryReadCache(String cacheKey) {
        try {
            String cached = scheduleStore.readCache(cacheKey);
            if (cached != null) {
                scheduleStore.hit();
                return objectMapper.readValue(cached, LIST_TYPE);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public List<ScheduleMainDto> tryReadCacheAgain(String cacheKey) {
        try {
            Thread.sleep(WAIT_BACKOFF.toMillis());
            return tryReadCache(cacheKey);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void tryWriteCache(String cacheKey, String lockKey, String requestId, List<ScheduleMainDto> value) {
        try {
            scheduleStore.writeCache(cacheKey, objectMapper.writeValueAsString(value));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean isForMain(LocalDateTime from, LocalDateTime to) {
        YearMonth fromYm = YearMonth.from(from);
        YearMonth toYm = YearMonth.from(to);
        return fromYm.equals(toYm.minusMonths(1))
                && from.equals(fromYm.atDay(1).atStartOfDay())
                && to.equals(fromYm.plusMonths(1).atDay(1).atStartOfDay());
    }
}