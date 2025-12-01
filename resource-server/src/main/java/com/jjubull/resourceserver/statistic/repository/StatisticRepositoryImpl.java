package com.jjubull.resourceserver.statistic.repository;

import com.jjubull.resourceserver.statistic.domain.QStatistic;
import com.jjubull.resourceserver.statistic.dto.command.MainInfoDto;
import com.jjubull.resourceserver.statistic.dto.command.StatisticDto;
import com.jjubull.resourceserver.statistic.dto.command.TodayInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class StatisticRepositoryImpl implements StatisticRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public StatisticDto getStatistic() {

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        Integer monthlySales = queryFactory.select(QStatistic.statistic.dailySales.sum())
                .from(QStatistic.statistic)
                .where(QStatistic.statistic.date.between(firstDayOfLastMonth, lastDayOfLastMonth))
                .fetchOne();

        return queryFactory.select(Projections.constructor(StatisticDto.class,
                QStatistic.statistic.dailyVisited, QStatistic.statistic.dailyReserved,
                QStatistic.statistic.dailySales, Expressions.constant(monthlySales)))
                .from(QStatistic.statistic)
                .where(QStatistic.statistic.date.eq(LocalDate.now().minusDays(1)))
                .fetchOne();
    }

    @Override
    public TodayInfoDto getTodayInfo() {

        return queryFactory.select(Projections.constructor(TodayInfoDto.class,
                QStatistic.statistic.dailyReserved, QStatistic.statistic.dailyDeposited))
                .from(QStatistic.statistic)
                .where(QStatistic.statistic.date.eq(LocalDate.now()))
                .fetchOne();
    }

    @Override
    public MainInfoDto getMainInfo() {

        return queryFactory.select(Projections.constructor(MainInfoDto.class,
                QStatistic.statistic.depositExpired, QStatistic.statistic.deposit24Hour))
                .from(QStatistic.statistic)
                .where(QStatistic.statistic.date.eq(LocalDate.now()))
                .fetchOne();
    }
}
