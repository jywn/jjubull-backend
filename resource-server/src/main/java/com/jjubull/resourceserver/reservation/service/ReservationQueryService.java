package com.jjubull.resourceserver.reservation.service;

import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationSimpleDto> getMyReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getReservations(userId, process, null, null, null, pageable);
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<ReservationSimpleDto> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable) {
        return reservationRepository.getReservations(null, process, from, to, shipId, pageable);
    }
}
