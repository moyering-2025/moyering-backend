package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.FollowDto;
import com.dev.moyering.socialing.service.FollowService;
import com.dev.moyering.user.dto.UserDto;
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
    @GetMapping("/followings")
    public ResponseEntity<List<UserDto>> getFollowings(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String search) {
//        Integer followerId = principal.getUser().getUserId();
//        List<FollowDto> followings = followService.getFollowings(followerId);
//        return ResponseEntity.ok(followings);
        try {
            Integer followerId = principal.getUser().getUserId();
        List<UserDto> followings = followService.getFollowings(followerId, page, size, search);
        return ResponseEntity.ok(followings);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 나를 팔로우한 목록
    @GetMapping("/followers")
    public ResponseEntity<List<UserDto>> getFollowers(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String search) {
//        System.out.println(page);
//        System.out.println(size);
//        System.out.println(search);
        try {
            Integer followingId = principal.getUser().getUserId();
            List<UserDto> followers = followService.getFollowers(followingId,page,size,search);
            System.out.println(followers);
            System.out.println(followingId);

//            System.out.println(followers);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
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
