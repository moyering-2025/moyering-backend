package com.dev.moyering.classring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.UserPaymentHistoryDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.service.ClassPaymentService;
import com.dev.moyering.common.dto.PageResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserClassRegisteredController {
	private final ClassPaymentService classPaymentService;
	
	@GetMapping("/user/mypage/myClassRegistList")
	public ResponseEntity<PageResponseDto<UserPaymentHistoryDto>> getUserPaymentHistory(
		  @RequestParam String tab,
		    @RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "5") int size,
		    @RequestParam(required = false) String keywords,
		    @AuthenticationPrincipal PrincipalDetails principal) {
	    UtilSearchDto dto = new UtilSearchDto();
	    dto.setTab(tab);
	    dto.setPage(page);
	    dto.setSize(size);
	    dto.setKeywords(keywords);
	    dto.setUserId(principal.getUser().getUserId());

	    try {
	        PageResponseDto<UserPaymentHistoryDto> result = classPaymentService.getUserPaymentHistory(dto);
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
	    }
		
	}
}
