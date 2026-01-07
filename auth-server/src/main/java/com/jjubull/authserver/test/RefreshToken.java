package com.jjubull.authserver.test;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Getter
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token_user_id")
    private Long userId;

    @Column(name = "refresh_token_value")
    private String value;

    @Column(name = "refresh_token_expires_at")
    private Instant expiresAt;

    private RefreshToken(Long userId, String value, Instant expiresAt) {
        this.userId = userId;
        this.value = value;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(Long userId, String value, Instant expiresAt) {
        return new RefreshToken(userId, value, expiresAt);
    }
}