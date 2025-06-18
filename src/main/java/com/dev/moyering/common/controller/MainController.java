package com.dev.moyering.common.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {
	private final HostClassService hostClassService;

    @GetMapping("/main")
    public ResponseEntity<List<HostClassDto>> getMainClasses(@RequestParam(required = false) Integer userId) {
        User user = null;
//        if (userId != null) {
//            user = userService.findById(userId); // 또는 .orElse(null) 형태로 예외 처리
//        }

        List<HostClassDto> classes;
		try {
			classes = hostClassService.getRecommendHostClassesForUser(userId);
	        return ResponseEntity.ok(classes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}

    }
}
