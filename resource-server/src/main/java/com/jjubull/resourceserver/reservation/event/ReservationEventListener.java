package com.jjubull.resourceserver.reservation.event;

import com.jjubull.resourceserver.message.service.MessageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final MessageCommandService messageCommandService;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleReservationCompleted(ReservationCompletedEvent event) {

        messageCommandService.sendMessage(
                "010-6346-1851",
                event.getReservationId() + " 예약을 성공하였습니다."
        );
    }
}