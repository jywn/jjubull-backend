package com.jjubull.resourceserver.schedule.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean tryReserve(Long scheduleId, int headCount) {
        int updated = jdbcTemplate.update("""
            UPDATE schedules s
            JOIN ships sh ON s.ship_id = sh.ship_id
            SET s.schedule_current_head_count = s.schedule_current_head_count + ?
            WHERE s.schedule_id = ?
            AND s.schedule_current_head_count + ? <= sh.max_head_count
        """, headCount, scheduleId, headCount);

        return updated == 1;
    }
}
