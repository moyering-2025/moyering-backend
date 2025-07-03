package com.dev.moyering.classring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.service.InquiryService;
import com.dev.moyering.host.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserInquiryController {
	private final ReviewService reviewService;
	private final InquiryService inquiryService;
	
	//클래스 상세 문의하기
	@PostMapping("/user/writeClassInquiry")
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
	
	// 클래스 상세 문의 리스트
    @GetMapping("/class/classInquiryList/{classId}") 
    public ResponseEntity<PageResponseDto<InquiryDto>> classInquiryList (
    		@PathVariable("classId") Integer classId,
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size) {
    	try {
    		PageResponseDto<InquiryDto> inquiryList = inquiryService.getInquiryListByClassId(classId, page, size);
            return ResponseEntity.ok(inquiryList);

		} catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
}
