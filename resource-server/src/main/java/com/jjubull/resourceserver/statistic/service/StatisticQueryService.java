package com.jjubull.resourceserver.statistic.service;

import com.jjubull.resourceserver.statistic.dto.command.MainInfoDto;
import com.jjubull.resourceserver.statistic.dto.command.StatisticDto;
import com.jjubull.resourceserver.statistic.dto.command.TodayInfoDto;
import com.jjubull.resourceserver.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticQueryService {

    private final StatisticRepository statisticRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public StatisticDto getStatistic() {
        return statisticRepository.getStatistic();
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public MainInfoDto getMainInfo() {
        return statisticRepository.getMainInfo();
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public TodayInfoDto getTodayInfo() {
        return statisticRepository.getTodayInfo();
    }
}
