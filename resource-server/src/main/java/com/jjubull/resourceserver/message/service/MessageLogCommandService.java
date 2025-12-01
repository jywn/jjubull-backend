package com.jjubull.resourceserver.message.service;

import com.jjubull.resourceserver.message.domain.MessageLog;
import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.repository.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageLogCommandService {

    private final MessageLogRepository messageLogRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void saveLog(List<String> receivers, String content, Result result) {
        List<MessageLog> logs = receivers.stream().map(receiver -> MessageLog.create(receiver, content, result)).toList();
        messageLogRepository.saveAll(logs);
    }
}
