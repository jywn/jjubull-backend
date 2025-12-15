package com.jjubull.resourceserver.message.service;

import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.exception.MessageSendFailedException;
import com.jjubull.resourceserver.schedule.domain.Status;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageCommandService {

    @Value("${solapi.sender}")
    private String senderNumber;

    private final DefaultMessageService defaultMessageService;
    private final MessageLogCommandService messageLogCommandService;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void sendMessages(List<String> receivers, Status status) {

        List<Message> messages = receivers.stream()
                .map(phone -> buildMessage(phone, status.message())).toList();

        try {
            defaultMessageService.send(messages);
            messageLogCommandService.saveLog(receivers, status.message(), Result.SUCCESS);
        } catch (SolapiEmptyResponseException | SolapiMessageNotReceivedException | SolapiUnknownException e) {
            messageLogCommandService.saveLog(receivers, status.message(), Result.FAILURE);
            throw new MessageSendFailedException(e);
        }
    }

    public void sendMessage(String receiver, String content) {

        Message message = buildMessage(receiver, content);

        try {
            defaultMessageService.send(message);
            //messageLogCommandService.saveLog(receiver, content, Result.SUCCESS);
        } catch (SolapiEmptyResponseException | SolapiMessageNotReceivedException | SolapiUnknownException e) {
            //messageLogCommandService.saveLog(receiver, content, Result.FAILURE);
            throw new MessageSendFailedException(e);
        }
    }

    @NotNull
    private Message buildMessage(String receiverNumber, String content) {
        Message msg = new Message();
        msg.setFrom(senderNumber);
        msg.setTo(receiverNumber);
        msg.setText(content);
        return msg;
    }
}
