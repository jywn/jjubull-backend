package com.jjubull.resourceserver.reservation.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationCompletedEvent {
    private final Long reservationId;
}