package com.jjubull.resourceserver.coupon.domain;

import com.jjubull.resourceserver.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_expires_at")
    private LocalDateTime expiresAt;
    
    public enum Type {
        WEEKDAY, // 평일
        WEEKEND, // 주말
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private Type type;

    public enum Status {
        AVAILABLE, // 사용 가능
        USED,      // 이미 사용됨
        EXPIRED    // 기간 만료됨
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status")
    private Status status = Status.AVAILABLE; // 기본값


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Coupon(LocalDateTime expiresAt, Type type, User user, Status status) {
        this.expiresAt = expiresAt;
        this.type = type;
        this.user = user;
        this.status = status;
    }

    public static Coupon create(Type type, User user) {
        return new Coupon(LocalDateTime.now().plusDays(30), type, user,  Status.AVAILABLE);
    }
}
