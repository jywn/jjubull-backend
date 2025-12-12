package com.jjubull.resourceserver.reservation.repository;

import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

public interface ReservationRepositoryCustom {
    Page<ReservationSimpleDto> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Long userId, Pageable pageable);
}
