package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.FollowDto;
import com.dev.moyering.socialing.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/socialing/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<FollowDto> follow(
            @PathVariable Integer followingId,
            @AuthenticationPrincipal PrincipalDetails principal) throws Exception {
        Integer followerId = principal.getUser().getUserId();
        FollowDto created = followService.follow(followerId, followingId);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollow(
            @PathVariable Integer followingId,
            @AuthenticationPrincipal PrincipalDetails principal) throws Exception {
        Integer followerId = principal.getUser().getUserId();
        followService.unfollow(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    // 내가 팔로우한 목록
    @GetMapping("/following")
    public ResponseEntity<List<FollowDto>> getFollowings(
            @AuthenticationPrincipal PrincipalDetails principal) {
        Integer followerId = principal.getUser().getUserId();
        List<FollowDto> followings = followService.getFollowings(followerId);
        return ResponseEntity.ok(followings);
    }

    // 나를 팔로우한 목록
    @GetMapping("/followers")
    public ResponseEntity<List<FollowDto>> getFollowers(
            @AuthenticationPrincipal PrincipalDetails principal) {
        Integer followingId = principal.getUser().getUserId();
        List<FollowDto> followers = followService.getFollowers(followingId);
        System.out.println(followers);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/status/{followingId}")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Integer followingId,
            @AuthenticationPrincipal PrincipalDetails principal) {
        Integer followerId = principal.getUser().getUserId();
        boolean status = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(status);
    }
}
