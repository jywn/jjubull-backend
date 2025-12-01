package com.jjubull.resourceserver.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.jjubull.resourceserver.coupon.domain.Coupon.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCouponDto {
    private Type type;
    private LocalDateTime expiresAt;
}
