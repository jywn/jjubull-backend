package com.jjubull.resourceserver.reservation.service;

import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationSimpleDto> getMyReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getReservations(process, null, null, null, userId, pageable);
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<ReservationSimpleDto> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Long userId, Pageable pageable) {
        return reservationRepository.getReservations(process, from, to, shipId, userId, pageable);
    }
}
