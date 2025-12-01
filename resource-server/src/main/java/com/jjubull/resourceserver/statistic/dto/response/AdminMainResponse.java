package com.jjubull.resourceserver.statistic.dto.response;

import com.jjubull.resourceserver.statistic.dto.command.MainInfoDto;
import com.jjubull.resourceserver.statistic.dto.command.StatisticDto;
import com.jjubull.resourceserver.statistic.dto.command.TodayInfoDto;
import com.jjubull.resourceserver.schedule.dto.command.TodayScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminMainResponse {

    TodayScheduleDto todaySchedule;
    TodayInfoDto todayInfo;
    MainInfoDto mainInfo;
    FinalStatisticDto statistic;

    private AdminMainResponse(TodayScheduleDto todaySchedule, TodayInfoDto todayInfo, MainInfoDto mainInfo, FinalStatisticDto statistic) {
        this.todaySchedule = todaySchedule;
        this.todayInfo = todayInfo;
        this.mainInfo = mainInfo;
        this.statistic = statistic;
    }

    public static AdminMainResponse from(TodayScheduleDto todaySchedule, TodayInfoDto todayInfo, MainInfoDto mainInfo, StatisticDto statistic) {
        return new AdminMainResponse(todaySchedule, todayInfo, mainInfo, FinalStatisticDto.from(statistic));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinalStatisticDto {
        int dailyVisited;
        double reservedRate;
        int dailySales;
        int monthlySales;

        private FinalStatisticDto(StatisticDto dto) {
            this.dailyVisited = dto.getDailyVisited();
            this.reservedRate = dto.getDailyVisited() == 0 ? 0 : Math.round(100 * (double) dto.getDailyReserved() / dto.getDailyVisited());
            this.dailySales = dto.getDailySales();
            this.monthlySales = dto.getMonthlySales();
        }

        public static FinalStatisticDto from(StatisticDto dto) {
            return new FinalStatisticDto(dto);
        }
    }
}
