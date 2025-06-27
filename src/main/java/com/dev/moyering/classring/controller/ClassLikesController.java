package com.dev.moyering.classring.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.classring.dto.ClassLikesDto;
import com.dev.moyering.classring.service.ClassLikesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ClassLikesController {
	private final ClassLikesService classLikesService;
	
	@PostMapping("/toggle-like")
    public ResponseEntity<Void> toggleLike(@RequestBody ClassLikesDto dto, @AuthenticationPrincipal PrincipalDetails principal) {
        dto.setUserId(principal.getUser().getUserId());
		try {
			classLikesService.toggleLike(dto);
	        return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }
	
	@GetMapping("/class-like-list")
	public ResponseEntity<List<ClassLikesDto>> userClassLikes( @AuthenticationPrincipal PrincipalDetails principal) {
		try {
			List<ClassLikesDto> classLikes = classLikesService.getClasslikeListByUserId(principal.getUser().getUserId());
			
	        return ResponseEntity.ok(classLikes);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
	}
}
