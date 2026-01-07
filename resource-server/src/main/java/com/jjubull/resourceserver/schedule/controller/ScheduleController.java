package com.jjubull.resourceserver.schedule.controller;

import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import com.jjubull.resourceserver.schedule.dto.response.ScheduleMainResponse;
import com.jjubull.resourceserver.schedule.service.ScheduleQueryService;
import com.jjubull.resourceserver.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleQueryService scheduleQueryService;
    private final ScheduleService scheduleService;

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<ScheduleMainResponse>> getSchedules(LocalDateTime from, LocalDateTime to) {
        List<ScheduleMainDto> schedules = scheduleService.getMainSchedules(from, to);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 목록 조회에 성공하였습니다.", ScheduleMainResponse.from(schedules)));
    }

    @GetMapping("/main/rdb")
    public ResponseEntity<ApiResponse<ScheduleMainResponse>> getSchedulesRdb(LocalDateTime from, LocalDateTime to) {
        List<ScheduleMainDto> schedules = scheduleQueryService.getSchedules(from, to);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 목록 조회에 성공하였습니다.", ScheduleMainResponse.from(schedules)));
    }
}
