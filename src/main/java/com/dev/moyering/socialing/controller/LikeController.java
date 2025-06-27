package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
