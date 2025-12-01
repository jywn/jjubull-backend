package com.jjubull.resourceserver.message.repository;

import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.dto.command.MessageLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MessageLogRepositoryCustom {
    Page<MessageLogDto> findMessageLog(LocalDateTime from, LocalDateTime to, Result result, Pageable pageable);
}
