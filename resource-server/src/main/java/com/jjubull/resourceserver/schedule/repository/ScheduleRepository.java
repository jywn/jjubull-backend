package com.jjubull.resourceserver.schedule.repository;

import com.jjubull.resourceserver.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    @Query("""
        SELECT sh.price
        FROM Schedule s
        JOIN s.ship sh
        WHERE s.id = :scheduleId
    """)
    int findShipPriceByScheduleId(@Param("scheduleId") Long scheduleId);
}
