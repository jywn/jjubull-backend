package com.jjubull.resourceserver.schedule.service;

import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.getSchedules(from, to);
    }
}
