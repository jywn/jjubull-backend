package com.jjubull.resourceserver.reservation.repository;

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

import static com.jjubull.common.domain.QUser.*;
import static com.jjubull.resourceserver.reservation.domain.QReservation.*;
import static com.jjubull.resourceserver.schedule.domain.QSchedule.*;
import static com.jjubull.resourceserver.ship.domain.QShip.*;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 종윤이 코드
     */
    // 예약자 이름, 인원, 메모,
    @Override
    public Page<ReservationSimpleDto> getReservations(Process process,
                                                      LocalDateTime from, LocalDateTime to,
                                                      Long shipId, Long userId, Pageable pageable) {
        List<ReservationSimpleDto> mainQuery = queryFactory
                .select(Projections.constructor(ReservationSimpleDto.class,
                        reservation.totalPrice, reservation.process,
                        reservation.headCount, reservation.request,
                        user.username, ship.fishType, schedule.departure))
                .from(schedule)
                .join(schedule.ship, ship)
                .join(reservation).on(reservation.schedule.eq(schedule))
                .join(reservation.user, user)
                .where(shipIdEq(shipId), fromGoe(from), toLoe(to), userIdEq(userId), processEq(process))
                .orderBy(schedule.departure.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reservation.count())
                .from(schedule)
                .join(reservation).on(reservation.schedule.eq(schedule))
                .where(shipIdEq(shipId), fromGoe(from), toLoe(to), userIdEq(userId), processEq(process));

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }

    private BooleanExpression processEq(Process process) {
        return process == null ? null : reservation.process.eq(process);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : user.id.eq(userId);
    }

    private BooleanExpression fromGoe(LocalDateTime from) {
        return from == null ? null : schedule.departure.goe(from);
    }

    private BooleanExpression toLoe(LocalDateTime to) {
        return to == null ? null : schedule.departure.loe(to);
    }

    private BooleanExpression shipIdEq(Long shipId) {
        return shipId == null ? null : schedule.ship.id.eq(shipId);
    }

}
