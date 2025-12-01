package com.jjubull.resourceserver.coupon;

import com.jjubull.resourceserver.coupon.domain.Coupon;
import com.jjubull.resourceserver.coupon.dto.ProfileCouponDto;

public final class CouponMapper {

    private CouponMapper() {}

    public static ProfileCouponDto toProfileCouponDto(Coupon coupon) {
        return ProfileCouponDto.builder()
                .type(coupon.getType())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }

}
