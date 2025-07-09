package com.dev.moyering.socialing.controller;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.config.jwt.JwtUtil;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.socialing.service.FeedService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class FeedController {

    private final FeedService feedService;
    private final UserRepository userRepository;
    private final LikeListRepository likeListRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/socialing/feeds")
    public ResponseEntity<List<FeedDto>> getFeeds(
            @RequestParam(defaultValue = "all") String sort,
//            @RequestParam(required = false) String userId
            @RequestHeader(value = "Authorization", required = false) String header
    ) {
//        System.out.println("==== CONTROLLER /socialing/feeds ====");
//        System.out.println("▶▶▶ Authorization header = " + header);
        Integer userId = jwtUtil.extractUserIdFromHeader(header);
//        System.out.println("userId = " + userId);

        List<FeedDto> feeds = null;
        try {
            feeds = feedService.getFeeds(sort, userId);
            return new ResponseEntity<>(feeds, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/socialing/feed")
    public ResponseEntity<FeedDto> getFeedDetail(
            @RequestParam Integer feedId,
            @AuthenticationPrincipal PrincipalDetails principal) {

        Integer currentUserId = principal != null
                ? principal.getUser().getUserId()
                : null;

        FeedDto dto = feedService.getFeedDetail(feedId, currentUserId);
//        System.out.println(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/user/socialing/feed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedDto> createFeed(
            @RequestPart("feed") FeedDto feedDto,
//            @ModelAttribute FeedDto feedDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        try {
//            System.out.println("이거 확인해야함!!" +principal.getUser().getNickName());
            feedDto.setWriterId(principal.getUser().getNickName());
            Integer created = feedService.createFeed(feedDto, images);
            FeedDto nFeedDto = feedService.getFeedDetail(created, principal.getUser().getUserId());
            return new ResponseEntity<>(nFeedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/socialing/memberFeed/{nickName}")
    public ResponseEntity<List<FeedDto>> getFeedsByNickName(
            @PathVariable("nickName") String nickName,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Integer userId = principal != null ? principal.getUser().getUserId() : null;
        try {
            List<FeedDto> feeds = feedService.getFeedsByNickname(nickName, userId);
            return new ResponseEntity<>(feeds, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/user/socialing/feed/{feedId}")
    public ResponseEntity<Void> updateFeed(
            @PathVariable Integer feedId,
//            @RequestPart("feed") FeedDto feedDto,
            @RequestPart String text,
//            @RequestPart String[] tags,
            @RequestPart(required = false) String tag1,
            @RequestPart(required = false) String tag2,
            @RequestPart(required = false) String tag3,
            @RequestPart(required = false) String tag4,
            @RequestPart(required = false) String tag5,
            @RequestPart(value = "img1", required = false) MultipartFile image1,
            @RequestPart(value = "img2", required = false) MultipartFile image2,
            @RequestPart(value = "img3", required = false) MultipartFile image3,
            @RequestPart(value = "img4", required = false) MultipartFile image4,
            @RequestPart(value = "img5", required = false) MultipartFile image5,
            @RequestParam(value = "removeUrls", required = false) String[] removeUrls,
            @AuthenticationPrincipal PrincipalDetails principal
            , HttpServletRequest request
    ) throws Exception {
        List<MultipartFile> images = new ArrayList<>();
        if (image1 != null && !image1.isEmpty()) images.add(image1);
        if (image2 != null && !image2.isEmpty()) images.add(image2);
        if (image3 != null && !image3.isEmpty()) images.add(image3);
        if (image4 != null && !image4.isEmpty()) images.add(image4);
        if (image5 != null && !image5.isEmpty()) images.add(image5);
        List<String> removeList = removeUrls != null ? Arrays.asList(removeUrls) : Collections.emptyList();
        System.out.println("img1 = " + image1);
        System.out.println("img2 = " + image2);
        System.out.println("img2 = " + image3);
        System.out.println("img2 = " + image4);
        System.out.println("img2 = " + image5);
//        System.out.println("==========================================" + removeUrls);
        System.out.println("백엔드에서 받은 removeUrls = " + removeUrls);
        FeedDto existing = feedService.getFeedDetail(feedId, principal.getUser().getUserId());
        if (!existing.getWriterUserId().equals(principal.getUser().getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        feedService.updateFeed(feedId, text, tag1, tag2, tag3, tag4, tag5, image1, image2, image3, image4, image5, removeList);
//        System.out.println(">>>> Content-Type: " + request.getContentType());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/socialing/feeds/{feedId}/liked")
    public ResponseEntity<Map<String, Boolean>> isFeedLiked(
            @PathVariable Integer feedId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        // 1) 로그인 안 된 경우 401
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2) 서비스에 위임
        Integer userId = principal.getUser().getUserId();
        boolean liked = feedService.isLikedByUser(feedId, userId);

        // 3) {"liked": true/false} 형태로 응답
        return ResponseEntity.ok(Map.of("liked", liked));
    }

    @GetMapping("/socialing/feeds/myFeeds")
    public ResponseEntity<Map<String, Object>> getMyFeeds(@RequestParam Integer userId) {
        try {
            Map<String, Object> feedsByUserId = feedService.getFeedsByUserId(userId);
            return new ResponseEntity<>(feedsByUserId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/socialing/popular")
    public ResponseEntity<List<FeedDto>> getPopularFeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<FeedDto> popularFeeds = null;
        try {
            popularFeeds = feedService.getPopularFeeds(page, size);
            return new ResponseEntity<>(popularFeeds, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{feedId}")
    public ResponseEntity<String> deleteFeed(
            @PathVariable Integer feedId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        if (principal == null || principal.getUser() == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Integer userId = principal.getUser().getUserId();


        try {
            feedService.deleteFeed(feedId, userId);
            return ResponseEntity.ok("삭제 완료 (soft delete)");
        } catch (IllegalArgumentException e) {
            // 존재하지 않거나 권한 없을 때
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("삭제 실패: " + e.getMessage());
        }
    }
    
    @GetMapping("/socialing/feeds/myFeedsLikeCount")
    public ResponseEntity<Map<Integer,Integer>> myFeedsLikeCount(@RequestParam Integer userId){
    	try{
    		Map<Integer,Integer> map = feedService.myFeedsLikeCount(userId);
    		return new ResponseEntity<>(map,HttpStatus.OK);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("/socialing/subCount")
    public ResponseEntity<Map<String,Object>>subCount(@RequestParam Integer userId){
    	try {
    		Map<String,Object> map = feedService.userSubCount(userId);
    		return new ResponseEntity<>(map,HttpStatus.OK);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    
    
    
}
