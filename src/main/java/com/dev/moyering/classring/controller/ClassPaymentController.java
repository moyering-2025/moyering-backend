package com.dev.moyering.classring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.ClassPaymentResponseDto;
import com.dev.moyering.classring.service.ClassPaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/payment")
public class ClassPaymentController {
    private final ClassPaymentService classPaymentService;
    
    // 1. 클래스 결제 정보 요청
    @GetMapping("/{classId}/{selectedCalendarId}")
    public ResponseEntity<ClassPaymentResponseDto> getPaymentInfo(
            @PathVariable Integer classId,
            @PathVariable Integer selectedCalendarId,
            @AuthenticationPrincipal PrincipalDetails principal
            ) {
    	ClassPaymentResponseDto response;
		try {
			response = classPaymentService.getClassPaymentInfo(principal.getUser().getUserId(), classId, selectedCalendarId);
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }
}
