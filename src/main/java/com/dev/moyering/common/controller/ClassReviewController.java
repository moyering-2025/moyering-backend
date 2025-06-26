package com.dev.moyering.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassReviewController {
	private final ReviewService reviewService;

	//클래스 상세 리뷰 더보기
    @GetMapping("/classRingReviewList/{hostId}") 
    public ResponseEntity<PageResponseDto<ReviewDto>> classRingReviewList (
    		@PathVariable("hostId") Integer hostId,
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size) {
    	try {
    		PageResponseDto<ReviewDto> reviewList = reviewService.getAllReviewByHostId(hostId, page, size);
            return ResponseEntity.ok(reviewList);

		} catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
}
