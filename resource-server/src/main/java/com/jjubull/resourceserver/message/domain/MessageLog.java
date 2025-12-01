package com.jjubull.resourceserver.message.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "message_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " message_log_id")
    private Long id;

    @Column(name = "message_log_time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "message_log_recipient_phone")
    private String recipientPhone;

    @Column(name = "message_log_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_log_result")
    private Result result;

    private MessageLog(LocalDateTime timeStamp, String recipientPhone, String content, Result result) {
        this.timeStamp = timeStamp;
        this.recipientPhone = recipientPhone;
        this.content = content;
        this.result = result;
    }

    public static MessageLog create(String recipientPhone, String content, Result result) {
        return new MessageLog(LocalDateTime.now(), recipientPhone, content, result);
    }
}
