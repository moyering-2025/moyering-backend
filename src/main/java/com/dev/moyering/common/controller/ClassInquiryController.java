package com.dev.moyering.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassInquiryController {
	private final InquiryService inquiryService;
	
    // 클래스 상세 문의 리스트
    @GetMapping("/classInquiryList/{classId}") 
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
