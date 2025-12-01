package com.jjubull.resourceserver.user.dto.response;

import com.jjubull.resourceserver.coupon.dto.ProfileCouponDto;
import com.jjubull.resourceserver.user.dto.command.MyProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileResponse {

    MyProfileDto profile;
    List<ProfileCouponDto> coupons;

    public static MyProfileResponse of(MyProfileDto profile, List<ProfileCouponDto> coupons) {
        return new MyProfileResponse(profile, coupons);
    }
}
