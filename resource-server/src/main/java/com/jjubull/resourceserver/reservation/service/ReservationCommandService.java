package com.jjubull.resourceserver.reservation.service;

import com.jjubull.common.domain.User;
import com.jjubull.common.exception.UserNotFoundException;
import com.jjubull.resourceserver.message.service.MessageCommandService;
import com.jjubull.resourceserver.reservation.domain.Reservation;
import com.jjubull.resourceserver.reservation.event.ReservationCompletedEvent;
import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.schedule.exception.NoPossibleSeatException;
import com.jjubull.resourceserver.schedule.repository.ScheduleJdbcRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleStore;
import com.jjubull.resourceserver.ship.domain.Ship;
import com.jjubull.resourceserver.ship.repository.ShipRepository;
import com.jjubull.resourceserver.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ScheduleStore scheduleStore;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleJdbcRepository scheduleJdbcRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void reserve(Long scheduleId, Long userId, int headCount, String request) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(EntityNotFoundException::new);

        if (!scheduleJdbcRepository.tryReserve(scheduleId, headCount)) {
            throw new NoPossibleSeatException();
        }

        String cacheKey = "cache:main:" + YearMonth.from(schedule.getDeparture()).toString();
        scheduleStore.evict(cacheKey);

        User user = userRepository.getReferenceById(userId);
        int price = scheduleRepository.findShipPriceByScheduleId(scheduleId);

        Reservation reservation = Reservation.create(headCount, request, price * headCount,
                Reservation.Process.RESERVE_COMPLETED, user, schedule);

        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationCompletedEvent(reservation.getId()));
    }
}
