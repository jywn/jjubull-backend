package com.jjubull.resourceserver.schedule.repository;

import com.jjubull.resourceserver.schedule.domain.QSchedule;
import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import com.jjubull.resourceserver.schedule.dto.command.TodayScheduleDto;
import com.jjubull.resourceserver.ship.domain.QShip;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {

        List<ScheduleMainDto> mainQuery = queryFactory
                .select(Projections.constructor(ScheduleMainDto.class,
                        QSchedule.schedule.departure,
                        QShip.ship.maxHeadCount.subtract(QSchedule.schedule.currentHeadCount),
                        QSchedule.schedule.tide, QSchedule.schedule.status, QSchedule.schedule.type,
                        QShip.ship.fishType, QShip.ship.price))
                .from(QSchedule.schedule)
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(QSchedule.schedule.ship.id))
                .where(QSchedule.schedule.departure.after(from).and(QSchedule.schedule.departure.before(to)))
                .orderBy(QSchedule.schedule.departure.asc())
                .fetch();

        return mainQuery;
    }

    public TodayScheduleDto getTodaySchedule() {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return  queryFactory
                .select(Projections.constructor(TodayScheduleDto.class,
                        QSchedule.schedule.currentHeadCount, QSchedule.schedule.departure,
                        QSchedule.schedule.status,
                        Projections.constructor(TodayScheduleDto.ShipDto.class,
                                QSchedule.schedule.ship.fishType, QSchedule.schedule.ship.price,
                                QSchedule.schedule.ship.notification, QSchedule.schedule.ship.maxHeadCount)))
                .from(QSchedule.schedule)
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(QSchedule.schedule.ship.id))
                .where(QSchedule.schedule.departure.goe(start), QSchedule.schedule.departure.lt(end))
                .orderBy(QSchedule.schedule.departure.desc())
                .limit(1)
                .fetchOne();
    }
}
