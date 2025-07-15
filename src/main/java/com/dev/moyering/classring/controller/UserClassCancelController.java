package com.dev.moyering.classring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.service.ClassPaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserClassCancelController {
	private final ClassPaymentService classPaymentService;
	
	@PutMapping("/user/class/cancel/{paymentId}")
	public ResponseEntity<?> updateStatus(@PathVariable Integer paymentId, @AuthenticationPrincipal PrincipalDetails principal) {
		try {
			classPaymentService.cancelClass(paymentId, principal.getUser().getUserId());
		    return ResponseEntity.ok().build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
		}
	}
}
