package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/socialing/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{feedId}")
    public ResponseEntity<?> toggleLike(@PathVariable Integer feedId,
                                        @AuthenticationPrincipal PrincipalDetails principal) {
        try {
            Integer userId = principal.getUser().getUserId();
            likeService.toggleLike(feedId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<FeedDto>> getFeedsWithLikeStatus(@AuthenticationPrincipal PrincipalDetails principal)  {

        Integer userId = (principal != null) ? principal.getUser().getUserId() : null;

        try {
            List<FeedDto> feeds = likeService.getFeedsWithLikeStatus(userId);
            return ResponseEntity.ok().body(feeds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
