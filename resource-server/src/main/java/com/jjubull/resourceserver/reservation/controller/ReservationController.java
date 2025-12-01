package com.jjubull.resourceserver.reservation.controller;

import com.jjubull.resourceserver.common.dto.response.ApiResponse;
import com.jjubull.resourceserver.common.dto.response.PageResponse;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import com.jjubull.resourceserver.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationQueryService reservationQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable) {

        Page<ReservationSimpleDto> pageResult = reservationQueryService.getReservations(process, from, to, shipId, pageable);

        return ResponseEntity.ok(ApiResponse.success("예약 목록 조회에 성공하였습니다.", PageResponse.from(pageResult)));
    }
}

