package com.dev.moyering.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.classring.service.UserCouponService;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.user.dto.MyScheduleResponseDto;
import com.dev.moyering.user.service.MyScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
public class MyPageController {
	private final MyScheduleService myScheduleService;
	private final UserCouponService couponService;
    @GetMapping("/schedule")
    public ResponseEntity<MyScheduleResponseDto> getMySchedule(@AuthenticationPrincipal PrincipalDetails principal) {
    	Integer userId = principal.getUser().getUserId();
        MyScheduleResponseDto dto;
		try {
			dto = myScheduleService.getMySchedule(userId);
	        return ResponseEntity.ok(dto);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }

    @GetMapping("/myCouponList")
    public ResponseEntity<PageResponseDto<UserCouponDto>> getMyCoupon(@AuthenticationPrincipal PrincipalDetails principal,
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size) {
    	
        System.out.println(principal+"sdlfkjhlsijdfsjflj");
    	Integer userId = principal.getUser().getUserId();
        System.out.println("sdlfkjhlsijdfsjflj");
        PageResponseDto<UserCouponDto> dto;
		try {
			dto = couponService.getUserCoponByUserId(userId,page, size);
	        return ResponseEntity.ok(dto);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }
}
