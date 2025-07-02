package com.dev.moyering.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.user.dto.MyScheduleResponseDto;
import com.dev.moyering.user.service.MyScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
public class MyPageController {
	private final MyScheduleService myScheduleService;
	
    @GetMapping("/schedule")
    public ResponseEntity<MyScheduleResponseDto> getMySchedule(@AuthenticationPrincipal PrincipalDetails principal) {
        System.out.println(principal+"sdlfkjhlsijdfsjflj");
    	Integer userId = principal.getUser().getUserId();
        System.out.println("sdlfkjhlsijdfsjflj");
        MyScheduleResponseDto dto;
		try {
			dto = myScheduleService.getMySchedule(userId);
	        return ResponseEntity.ok(dto);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }

}
