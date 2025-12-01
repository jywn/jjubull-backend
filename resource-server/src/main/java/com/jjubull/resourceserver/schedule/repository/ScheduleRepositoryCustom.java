package com.jjubull.resourceserver.schedule.repository;

import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import com.jjubull.resourceserver.schedule.dto.command.TodayScheduleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to);
    public TodayScheduleDto getTodaySchedule();
}
