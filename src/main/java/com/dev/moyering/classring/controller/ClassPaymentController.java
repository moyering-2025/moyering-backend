package com.dev.moyering.classring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.ClassPaymentResponseDto;
import com.dev.moyering.classring.dto.PaymentApproveRequestDto;
import com.dev.moyering.classring.dto.PaymentInitRequestDto;
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
    
    //결제 준비 
    @PostMapping("/init")
    public ResponseEntity<Void> init(@RequestBody PaymentInitRequestDto dto, @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            classPaymentService.initPayment(dto, principal.getUser());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

  
    //결제 처리
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(@RequestBody PaymentApproveRequestDto dto, @AuthenticationPrincipal PrincipalDetails principal) {
    	if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
			classPaymentService.approvePayment(dto,principal.getUser());
	    	return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }
}
