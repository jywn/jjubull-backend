package com.jjubull.resourceserver.coupon.service;

import com.jjubull.resourceserver.coupon.domain.Coupon;
import com.jjubull.resourceserver.coupon.CouponMapper;
import com.jjubull.resourceserver.coupon.dto.ProfileCouponDto;
import com.jjubull.resourceserver.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponQueryService {

    private final CouponRepository couponRepository;

    public List<ProfileCouponDto> getProfileCoupons(Long userId) {
        List<Coupon> coupons = couponRepository.findByUserIdAndStatus(userId, Coupon.Status.AVAILABLE);
        return coupons.stream().map(CouponMapper::toProfileCouponDto).toList();
    }
}
