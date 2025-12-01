package com.jjubull.resourceserver.user.controller;

import com.jjubull.resourceserver.common.dto.response.ApiResponse;
import com.jjubull.resourceserver.common.dto.response.PageResponse;
import com.jjubull.resourceserver.coupon.dto.ProfileCouponDto;
import com.jjubull.resourceserver.coupon.service.CouponQueryService;
import com.jjubull.resourceserver.reservation.service.ReservationQueryService;
import com.jjubull.resourceserver.reservation.dto.command.ReservationSimpleDto;
import com.jjubull.resourceserver.common.AuthenticatedUser;
import com.jjubull.resourceserver.user.domain.User;
import com.jjubull.resourceserver.user.dto.command.MyProfileDto;
import com.jjubull.resourceserver.user.dto.response.MyProfileResponse;
import com.jjubull.resourceserver.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.jjubull.resourceserver.reservation.domain.Reservation.Process;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;
    private final CouponQueryService couponQueryService;

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        User user = userQueryService.findUser(authenticatedUser.getProvider(), authenticatedUser.getSub());

        List<ProfileCouponDto> profileCoupons = couponQueryService.getProfileCoupons(user.getId());

        MyProfileDto myProfile = userQueryService.getMyProfile(user.getId());

        MyProfileResponse response = MyProfileResponse.of(myProfile, profileCoupons);

        return ResponseEntity.ok(ApiResponse.success("내 프로필 조회에 성공하였습니다.", response));
    }

    //내 예약 검색하기
    @GetMapping("/me/reservations")
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getMyReservations(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                             Process process, Pageable pageable) {

        User user = userQueryService.findUser(authenticatedUser.getProvider(), authenticatedUser.getSub());

        Page<ReservationSimpleDto> pageResult = reservationQueryService.getMyReservations(user.getId(), process, pageable);

        return ResponseEntity.ok(ApiResponse.success("나의 예약 목록 조회를 성공하였습니다.", PageResponse.from(pageResult)));
    }
}
