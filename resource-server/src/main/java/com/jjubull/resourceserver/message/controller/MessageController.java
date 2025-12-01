package com.jjubull.resourceserver.message.controller;

import com.jjubull.resourceserver.common.dto.response.ApiResponse;
import com.jjubull.resourceserver.message.service.MessageCommandService;
import com.jjubull.resourceserver.schedule.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageCommandService messageCommandService;
    private final List<String> dummyPhones = new ArrayList<>();

    @PostMapping("/sms")
    public ResponseEntity<ApiResponse<Void>> sendForCanceled() {

        for (int i = 0; i < 10; i++) dummyPhones.add("010-6346-1851");

        messageCommandService.sendMessage(dummyPhones, Status.CANCELED);

        return ResponseEntity.ok(ApiResponse.success("문자 전송에 성공하였습니다"));
    }
}
