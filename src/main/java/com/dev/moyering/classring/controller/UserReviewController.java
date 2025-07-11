package com.dev.moyering.classring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.service.ReviewService;
import com.dev.moyering.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserReviewController {
	private final ReviewService reviewService;

	//클래스 상세 리뷰 더보기
    @GetMapping("/class/classRingReviewList/{classId}") 
    public ResponseEntity<PageResponseDto<ReviewDto>> classRingReviewList (
    		@PathVariable("classId") Integer classId,
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size) {
    	try {
    		PageResponseDto<ReviewDto> reviewList = reviewService.getAllReviewByClassId(classId, page, size);
            return ResponseEntity.ok(reviewList);

		} catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
    
    //마이페이지 클래스 리뷰 => 작성 가능한 것
    @PostMapping("/user/mypage/reviewList/writable")
    public ResponseEntity<PageResponseDto<WritableReviewResponseDto>> getWritableReviewList(
        @RequestBody UtilSearchDto dto,
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        dto.setUserId(principal.getUser().getUserId());
        try {
        	PageResponseDto<WritableReviewResponseDto> result = reviewService.getWritableReviews(dto); // ↓서비스 분리
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //마이페이지 리뷰 작성 완료한 것
    @PostMapping("/user/mypage/reviewList/done")
    public ResponseEntity<PageResponseDto<UserReviewResponseDto>> getDoneReviewList(
        @RequestBody UtilSearchDto dto,
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        dto.setUserId(principal.getUser().getUserId());
        try {
        	PageResponseDto<UserReviewResponseDto> result = reviewService.getDoneReviews(dto); // ↓서비스 분리
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    //마이페이지 리뷰 작성하기
    @PostMapping("/user/mypage/write-review")
    public ResponseEntity<Integer> uploadReview(
		@ModelAttribute ReviewDto reviewDto,
        @AuthenticationPrincipal PrincipalDetails principal
    ) {    	
    	User user = principal.getUser(); 
        reviewDto.setUserId(user.getUserId());
        reviewDto.setStudentName(user.getNickName());
        try {
        	Integer reviewId = reviewService.writeReview(reviewDto); // ↓서비스 분리
            return ResponseEntity.ok(reviewId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
