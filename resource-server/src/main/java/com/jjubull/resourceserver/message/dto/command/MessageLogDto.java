package com.jjubull.resourceserver.message.dto.command;

import com.jjubull.resourceserver.message.domain.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MessageLogDto {

    private String phone;
    private String content;
    private LocalDateTime timeStamp;
    private Result result;
}
