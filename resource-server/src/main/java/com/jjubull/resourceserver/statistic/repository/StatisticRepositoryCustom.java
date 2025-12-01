package com.jjubull.resourceserver.statistic.repository;

import com.jjubull.resourceserver.statistic.dto.command.MainInfoDto;
import com.jjubull.resourceserver.statistic.dto.command.StatisticDto;
import com.jjubull.resourceserver.statistic.dto.command.TodayInfoDto;

public interface StatisticRepositoryCustom {
    TodayInfoDto getTodayInfo();
    StatisticDto getStatistic();
    MainInfoDto getMainInfo();
}
