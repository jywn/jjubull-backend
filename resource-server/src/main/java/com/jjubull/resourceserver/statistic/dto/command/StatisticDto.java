package com.jjubull.resourceserver.statistic.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {

    int dailyVisited;
    int dailyReserved;
    int dailySales;
    int monthlySales;

}
