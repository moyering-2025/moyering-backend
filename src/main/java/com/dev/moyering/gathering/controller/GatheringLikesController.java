package com.dev.moyering.gathering.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.gathering.dto.GatheringLikesDto;
import com.dev.moyering.gathering.service.GatheringLikesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class GatheringLikesController {
	private final GatheringLikesService gatheringLikesService;
	
	@PostMapping("/gather-toggle-like")
    public ResponseEntity<Void> toggleLike(@RequestBody GatheringLikesDto dto, @AuthenticationPrincipal PrincipalDetails principal) {
		dto.setUserId(principal.getUser().getUserId());
		System.out.println(dto);
		try {
			System.out.println(dto.getGatheringLikeId());
			gatheringLikesService.toggleGatheringLike(principal.getUser().getUserId(), dto.getGatheringId());
	        return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
    }
	
	@GetMapping("/gather-like-list")
	public ResponseEntity<List<GatheringLikesDto>> userGatherLikes( @AuthenticationPrincipal PrincipalDetails principal) {
		try {
			List<GatheringLikesDto> gatherLikes = gatheringLikesService.getGatherlikeListByUserId(principal.getUser().getUserId());
	        return ResponseEntity.ok(gatherLikes);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}
	}
}
