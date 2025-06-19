package com.dev.moyering.socialing.controller;

import com.dev.moyering.socialing.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/socialing/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{feedId}")
    public ResponseEntity<?> toggleLike(@PathVariable Integer feedId, @RequestParam String username) {
        try {
            likeService.toggleLike(feedId, username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
