package com.jjubull.resourceserver.reservation.repository;

import com.jjubull.resourceserver.reservation.domain.QReservation;
import com.jjubull.resourceserver.schedule.domain.QSchedule;
import com.jjubull.resourceserver.ship.domain.QShip;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 종윤이 코드
     */
    // 예약자 이름, 인원, 메모,
    @Override
    public Page<ReservationSimpleDto> getReservations(Long userId, Process process,
                                                      LocalDateTime from, LocalDateTime to,
                                                      Long shipId, Pageable pageable) {
        List<ReservationSimpleDto> mainQuery = queryFactory
                .select(Projections.constructor(ReservationSimpleDto.class,
                        QReservation.reservation.totalPrice, QReservation.reservation.process,
                        QReservation.reservation.headCount, QReservation.reservation.request,
                        QReservation.reservation.user.username,
                        QReservation.reservation.schedule.ship.fishType,
                        QReservation.reservation.schedule.departure))
                .from(QReservation.reservation)
                .leftJoin(QSchedule.schedule).on(QSchedule.schedule.id.eq(QReservation.reservation.schedule.id))
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(QReservation.reservation.schedule.id))
                .where(userIdEq(userId), processEq(process), fromGoe(from), toLoe(to), shipIdEq(shipId))
                .orderBy(QReservation.reservation.schedule.departure.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QReservation.reservation.count())
                .from(QReservation.reservation)
                .where(userIdEq(userId), processEq(process), fromGoe(from), toLoe(to), shipIdEq(shipId));

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }

    private BooleanExpression processEq(Process process) {
        return process == null ? null : QReservation.reservation.process.eq(process);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : QReservation.reservation.user.id.eq(userId);
    }

    private BooleanExpression fromGoe(LocalDateTime from) {
        return from == null ? null : QReservation.reservation.schedule.departure.goe(from);
    }

    private BooleanExpression toLoe(LocalDateTime to) {
        return to == null ? null : QReservation.reservation.schedule.departure.loe(to);
    }

    private BooleanExpression shipIdEq(Long shipId) {
        return shipId == null ? null : QReservation.reservation.schedule.ship.id.eq(shipId);
    }

}
