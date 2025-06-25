package com.dev.moyering.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.service.ClassCouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCouponController {
	private ClassCouponService classCouponService;
	
	@PostMapping("/classCoupons/download")
	public ResponseEntity<?> downloadClassCoupon(@RequestBody ClassCouponDto dto,
            @AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println("ddddddddddddddddddddddddddddddclassCouponId"+dto.getClassCouponId());
	    Integer userId = principal.getUser().getUserId();
	    Integer classCouponId = dto.getClassCouponId();

	    try {
	    	classCouponService.downloadClassCoupon(userId, classCouponId);
	        return ResponseEntity.ok("쿠폰 다운로드 완료");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("다운로드 실패: " + e.getMessage());
	    }
	}
}
