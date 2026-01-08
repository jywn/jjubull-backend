package com.jjubull.resourceserver.reservation.service;

import com.jjubull.common.domain.User;
import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.schedule.exception.NoPossibleSeatException;
import com.jjubull.resourceserver.schedule.repository.ScheduleJdbcRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import com.jjubull.resourceserver.schedule.repository.ScheduleStore;
import com.jjubull.resourceserver.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationCommandServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ScheduleJdbcRepository scheduleJdbcRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    ScheduleStore scheduleStore;

    @InjectMocks
    ReservationCommandService reservationCommandService;

    @Test
    @DisplayName("예약이_실패하면_예외_발생")
    void reserve_fail() {
        // given
        when(scheduleJdbcRepository.tryReserve(1L, 1))
                .thenReturn(false);

        Schedule mockSchedule = mock(Schedule.class);

        when(scheduleRepository.findById(1L))
                .thenReturn(Optional.of(mockSchedule));

    // when & then
        assertThrows(
                NoPossibleSeatException.class,
                () -> reservationCommandService.reserve(1L, 1L, 1, "테스트")
        );
    }

    @Test
    @DisplayName("예약이_성공하면_저장_호출")
    void reserve_success() {
        // given
        Long scheduleId = 1L;
        Long userId = 1L;

        Schedule mockSchedule = mock(Schedule.class);
        when(mockSchedule.getDeparture()).thenReturn(LocalDateTime.now());

        when(scheduleRepository.findById(1L))
                .thenReturn(Optional.of(mockSchedule));

        when(scheduleJdbcRepository.tryReserve(scheduleId, 1))
                .thenReturn(true);

        when(userRepository.getReferenceById(userId))
                .thenReturn(mock(User.class));

        when(scheduleRepository.findShipPriceByScheduleId(scheduleId))
                .thenReturn(10000);

        // when
        reservationCommandService.reserve(scheduleId, userId, 1, "요청");

        // then
        verify(reservationRepository).save(any());
    }

}