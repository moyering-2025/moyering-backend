package com.dev.moyering.classring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.service.UserCouponService;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.service.ClassCouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCouponController {
	private final UserCouponService userCouponService;
	
	@PostMapping("/classCoupons/download")
	public ResponseEntity<?> downloadClassCoupon(@RequestBody ClassCouponDto dto,
            @AuthenticationPrincipal PrincipalDetails principal) {
		
	    Integer userId = principal.getUser().getUserId();
	    Integer classCouponId = dto.getClassCouponId();

	    try {
	    	if (userId != null ) {
		    	userCouponService.downloadClassCoupon(userId, classCouponId);
		        return ResponseEntity.ok("쿠폰 다운로드 완료");
	    	} else {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("다운로드 실패: 로그인필요" );
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("다운로드 실패: " + e.getMessage());
	    }
	}
}
