package com.jjubull.resourceserver.schedule.dto.command;

import com.jjubull.resourceserver.schedule.domain.Status;
import com.jjubull.resourceserver.schedule.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleMainDto {

    // schedule
    private LocalDateTime departure;
    private Integer remainingHeadCount;
    private Integer tide;
    private Status status;
    private Type type;

    // ship
    private String fishType;
    private Integer price;
}
