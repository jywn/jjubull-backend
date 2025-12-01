package com.jjubull.resourceserver.statistic.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainInfoDto {

    int depositExpired;
    int deposit24Hour;

}
