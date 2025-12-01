package com.jjubull.resourceserver.reservation.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSimpleDto {

    Integer totalPrice;
    Process process;
    int headCount;
    String request;
    String username;

    String shipFishType;

    LocalDateTime scheduleDeparture;
}
