package com.jjubull.resourceserver.statistic.controller;

import com.jjubull.resourceserver.common.dto.response.ApiResponse;
import com.jjubull.resourceserver.statistic.dto.response.AdminMainResponse;
import com.jjubull.resourceserver.statistic.service.AdminService;
import com.jjubull.resourceserver.statistic.dto.command.MainInfoDto;
import com.jjubull.resourceserver.statistic.dto.command.StatisticDto;
import com.jjubull.resourceserver.statistic.dto.command.TodayInfoDto;
import com.jjubull.resourceserver.statistic.service.StatisticQueryService;
import com.jjubull.resourceserver.schedule.dto.command.TodayScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final AdminService adminService;
    private final StatisticQueryService statisticQueryService;

    @GetMapping("/admin/main")
    public ResponseEntity<ApiResponse<AdminMainResponse>> adminMain() {

        TodayScheduleDto todaySchedule = adminService.getTodaySchedule();
        TodayInfoDto todayInfo = statisticQueryService.getTodayInfo();
        MainInfoDto mainInfo = statisticQueryService.getMainInfo();
        StatisticDto statistic = statisticQueryService.getStatistic();

        AdminMainResponse response = AdminMainResponse.from(todaySchedule, todayInfo, mainInfo, statistic);

        return ResponseEntity.ok(ApiResponse.success("관리자 대시보드 조회에 성공하였습니다.", response));
    }
}
