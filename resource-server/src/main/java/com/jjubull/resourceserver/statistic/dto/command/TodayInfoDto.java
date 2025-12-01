package com.jjubull.resourceserver.statistic.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayInfoDto {

    int newReserved;
    int newDeposited;
}
