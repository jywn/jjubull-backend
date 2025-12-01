package com.jjubull.resourceserver.schedule.dto.response;

import com.jjubull.resourceserver.schedule.dto.command.ScheduleMainDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ScheduleMainResponse {

    List<ScheduleMainDto> schedules;

    private ScheduleMainResponse(List<ScheduleMainDto> dto) {
        this.schedules = dto;
    }

    public static ScheduleMainResponse from(List<ScheduleMainDto> dto) {
        return new ScheduleMainResponse(dto);
    }
}
