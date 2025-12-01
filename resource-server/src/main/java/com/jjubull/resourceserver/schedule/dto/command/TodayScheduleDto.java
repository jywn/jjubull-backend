package com.jjubull.resourceserver.schedule.dto.command;

import com.jjubull.resourceserver.schedule.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayScheduleDto {

    int currentHeadCount;
    LocalDateTime departure;
    Status status;
    ShipDto ship;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShipDto {
        String fishType;
        int price;
        String notification;
        int maxHeadCount;
    }
}
