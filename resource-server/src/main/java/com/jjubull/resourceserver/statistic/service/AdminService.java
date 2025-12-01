package com.jjubull.resourceserver.statistic.service;

import com.jjubull.resourceserver.schedule.dto.command.TodayScheduleDto;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ScheduleRepository scheduleRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public TodayScheduleDto getTodaySchedule() {
        return scheduleRepository.getTodaySchedule();
    }
}
