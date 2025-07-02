package com.dev.moyering.common.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.service.UserClassService;
import com.dev.moyering.common.dto.ClassRingDetailResponseDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.service.ClassCalendarService;
import com.dev.moyering.host.service.ClassCouponService;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.host.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassDetailController {

	private final HostClassService hostClassService;
	private final HostService hostService;
	private final ReviewService reviewService;
	private final ClassCouponService classCouponService; 
	private final ClassCalendarService classCalendarService;
	
	private final UserClassService userClassService;

	//클래스 상세페이지
    @GetMapping("/classRingDetail/{classId}") 
    public ResponseEntity<ClassRingDetailResponseDto> classRingDetail (@PathVariable("classId") Integer classId,
    		@AuthenticationPrincipal PrincipalDetails principal) {
    	System.out.println("==============="+principal);
    	try {
//			HostClassDto hostclass = hostClassService.getClassDetailByClassID(classId);
//	        List<ClassCalendarDto> classCalendar = classCalendarService.getClassCalendarByHostClassId(classId);
//	        HostDto host = hostService.getHostById(hostclass.getHostId());
//	        List<ReviewDto> reviews = reviewService.getReviewByHostId(hostclass.getHostId());
//	        List<ClassCouponDto> classCoupons = classCouponService.getCouponByClassId(classId);
//	        
//	        ClassRingDetailResponseDto result = ClassRingDetailResponseDto.builder()
//	                .hostClass(hostclass)
//	                .calendarList(classCalendar)
//	                .currList(classCalendar) // 혹시 다르면 따로 분리
//	                .host(host)
//	                .reviews(reviews)
//	                .coupons(classCoupons)
//	                .build();
//    		
    		  Integer userId = (principal != null)
    			      ? principal.getUser().getUserId()
    			      : null;

    		ClassRingDetailResponseDto result = userClassService.getClassRingDetail(classId, userId);
    		System.out.println(result.getCoupons());
	        return ResponseEntity.ok(result);

		} catch (Exception e) {
	        e.printStackTrace();   
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(null);		}
    }
}
