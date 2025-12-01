package com.jjubull.resourceserver.message.controller;

import com.jjubull.common.dto.response.ApiResponse;
import com.jjubull.common.dto.response.PageResponse;
import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.dto.command.MessageLogDto;
import com.jjubull.resourceserver.message.service.MessageLogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MessageLogController {

    private final MessageLogQueryService messageLogQueryService;

    @GetMapping("/sms/search")
    public ResponseEntity<ApiResponse<PageResponse<MessageLogDto>>> search(
            LocalDateTime from, LocalDateTime to, Result result, Pageable pageable
    ) {

        Page<MessageLogDto> pageResult = messageLogQueryService.findMessageLog(from, to, result, pageable);

        return ResponseEntity.ok(ApiResponse.success("문자 발송 내역 조회에 성공하였습니다.", PageResponse.from(pageResult)));
    }
}
