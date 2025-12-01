package com.jjubull.resourceserver.reservation.domain;

import com.jjubull.resourceserver.coupon.domain.Coupon;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_head_count")
    private int headCount;

    @Column(name = "reservation_request")
    private String request;

    @Column(name = "reservation_total_price")
    private int totalPrice;

    public enum Process {
        RESERVE_COMPLETED, // 예약 완료
        DEPOSIT_COMPLETED, // 입금 완료
        CANCEL_REQUESTED, // 취소 신청
        CANCEL_COMPLETED // 취소 완료
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    private Reservation(int headCount, String request, int totalPrice, Process process, User user, Schedule schedule, Coupon coupon) {
        this.headCount = headCount;
        this.request = request;
        this.totalPrice = totalPrice;
        this.process = process;
        this.user = user;
        this.schedule = schedule;
        this.coupon = coupon;
    }

    public static Reservation create(int headCount, String request, int totalPrice, Process process, User user, Schedule schedule, Coupon coupon) {
        return Reservation.builder()
                .headCount(headCount)
                .request(request)
                .totalPrice(totalPrice)
                .process(process)
                .user(user)
                .schedule(schedule)
                .coupon(coupon)
                .build();
    }
}
