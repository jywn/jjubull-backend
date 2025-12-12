package com.jjubull.resourceserver.reservation.controller;

import com.jjubull.common.domain.User;
import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.common.dto.response.PageResponse;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import com.jjubull.resourceserver.reservation.service.ReservationCommandService;
import com.jjubull.resourceserver.reservation.service.ReservationQueryService;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.schedule.dto.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReservationController {

    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;

    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Long userId, Pageable pageable) {

        Page<ReservationSimpleDto> pageResult = reservationQueryService.getReservations(process, from, to, shipId, userId, pageable);

        return ResponseEntity.ok(ApiResponse.success("예약 목록 조회에 성공하였습니다.", PageResponse.from(pageResult)));
    }

    @PostMapping("/schedules/{scheduleId}")
    public ResponseEntity<ApiResponse<Long>> reserve(@PathVariable("scheduleId") Long scheduleId, @RequestBody ReservationRequest reservationRequest) {

        // 경쟁 상태의 충돌 확인을 위해 각자 예약했다고 생각하는 번호를 반환한다.
        Long id = reservationCommandService.reserve(scheduleId, reservationRequest.getUserId(), reservationRequest.getHeadCount(), reservationRequest.getRequest());

        return ResponseEntity.ok(ApiResponse.success("예약에 성공하였습니다.", id));
    }
}

