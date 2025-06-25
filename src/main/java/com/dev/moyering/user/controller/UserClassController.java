package com.dev.moyering.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.service.InquiryService;
import com.dev.moyering.host.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserClassController {
	private final ReviewService reviewService;
	private final InquiryService inquiryService;
	
	@PostMapping("/writeClassInquiry")
	public ResponseEntity<Integer> writeClassInquiry(
			@RequestBody InquiryDto dto,@AuthenticationPrincipal PrincipalDetails principal) {
		
		Integer userId = principal.getUser().getUserId();
		dto.setUserId(userId);
		try {
			Integer inquiryId = inquiryService.writeInquriy(dto);
	        return ResponseEntity.ok(inquiryId); // 성공 시 생성된 ID 반환
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
		
	}
}
