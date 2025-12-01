package com.jjubull.resourceserver.message.service;

import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.dto.command.MessageLogDto;
import com.jjubull.resourceserver.message.repository.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageLogQueryService {

    private final MessageLogRepository messageLogRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<MessageLogDto> findMessageLog(LocalDateTime from, LocalDateTime to, Result result, Pageable pageable) {
        return messageLogRepository.findMessageLog(from, to, result, pageable);
    }
}
