package com.jjubull.resourceserver.reservation.service;

import com.jjubull.common.domain.User;
import com.jjubull.common.exception.UserNotFoundException;
import com.jjubull.resourceserver.reservation.domain.Reservation;
import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.schedule.domain.Schedule;
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

    public void reserve(Long scheduleId, Long userId, int headCount, String request) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(EntityNotFoundException::new);
        schedule.reserve(headCount);

        Ship ship = schedule.getShip();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Reservation reservation = Reservation.create(headCount, request, ship.getPrice() * headCount,
                Reservation.Process.RESERVE_COMPLETED, user, schedule);

        reservationRepository.save(reservation);
    }
}
