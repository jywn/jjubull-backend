package com.jjubull.resourceserver.schedule.dto.request;

import com.jjubull.common.domain.User;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    Long userId;
    int headCount;
    String request;
}
