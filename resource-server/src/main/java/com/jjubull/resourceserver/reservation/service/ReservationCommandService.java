package com.jjubull.resourceserver.reservation.service;

import com.jjubull.common.domain.User;
import com.jjubull.common.exception.UserNotFoundException;
import com.jjubull.resourceserver.message.service.MessageCommandService;
import com.jjubull.resourceserver.reservation.domain.Reservation;
import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.schedule.exception.NoPossibleSeatException;
import com.jjubull.resourceserver.schedule.repository.ScheduleJdbcRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import com.jjubull.resourceserver.ship.domain.Ship;
import com.jjubull.resourceserver.ship.repository.ShipRepository;
import com.jjubull.resourceserver.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationCommandService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleJdbcRepository scheduleJdbcRepository;
    private final MessageCommandService messageCommandService;

    public void reserve(Long scheduleId, Long userId, int headCount, String request) {

        if (!scheduleJdbcRepository.tryReserve(scheduleId, headCount)) {
            throw new NoPossibleSeatException();
        }

        Schedule schedule = scheduleRepository.getReferenceById(scheduleId);
        User user = userRepository.getReferenceById(userId);
        int price = scheduleRepository.findShipPriceByScheduleId(scheduleId);

        Reservation reservation = Reservation.create(headCount, request, price * headCount,
                Reservation.Process.RESERVE_COMPLETED, user, schedule);

        reservationRepository.save(reservation);
        messageCommandService.sendMessage("010-6346-1851", "예약을 성공하였습니다.");
    }
}
