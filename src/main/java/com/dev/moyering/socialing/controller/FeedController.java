package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.service.FeedService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class FeedController {

    private final FeedService feedService;
    private final UserRepository userRepository;

    @GetMapping("/socialing/feeds")
    public ResponseEntity<List<FeedDto>> getFeeds(
            @RequestParam(defaultValue = "all") String sort,
            @RequestParam(required = false) String userId
    ) {

        try {
            List<FeedDto> feeds = feedService.getFeeds(sort, userId);
            for (FeedDto feed : feeds) {
                feed.toEntity();
                System.out.println(feed.toEntity().getUser().getUserId());
            }
            return new ResponseEntity<>(feeds, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    // ✔ 피드 상세 조회
//    @GetMapping("/feeds/{feedId}")
//    public ResponseEntity<Map<String, Object>> getFeedDetail(@PathVariable Integer feedId,
//                                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        Map<String, Object> response = new HashMap<>();
//
//        // 1. 피드 본문
//        FeedDto feedDto = feedService.getFeedById(feedId);
//        response.put("feed", feedDto);
//
//        // 2. 댓글 목록
//        List<CommentDto> comments = feedService.getCommentsByFeedId(feedId);
//        response.put("comments", comments);
//
//        // 3. 좋아요 수
//        Long likeCount = feedService.getLikeCount(feedId);
//        response.put("likeCount", likeCount);
//
//        // 4. 로그인한 사용자의 좋아요 여부
//        String loginUserId = principalDetails != null ? principalDetails.getUsername() : null;
//        boolean isLiked = feedService.checkUserLikedFeed(feedId, loginUserId);
//        response.put("isLiked", isLiked);
//
//        // 5. 기타 (예: 뱃지 정보, 작성자 프로필 등 추가 가능)
//        response.put("writerBadge", feedDto.getWriterBadge());
//        response.put("writerProfile", feedDto.getWriterProfile());
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/socialing/feed")
    public ResponseEntity<FeedDto> getFeedDetail(
            @RequestParam Integer feedId,
            @AuthenticationPrincipal PrincipalDetails principal) {

        System.out.println(feedId);
        Integer currentUserId = principal != null
                ? principal.getUser().getUserId()
                : null;

        FeedDto dto = feedService.getFeedDetail(feedId, currentUserId);
        System.out.println(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/user/socialing/feed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedDto> createFeed(
            @RequestPart("feed") FeedDto feedDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        try {
            Integer created = feedService.createFeed(feedDto, images);
            FeedDto nFeedDto = feedService.getFeedDetail(created, principal.getUser().getUserId());
            return new ResponseEntity<>(nFeedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/socialing/userFeed/{nickName}")
    public ResponseEntity<List<FeedDto>> getUserByNickName(@PathVariable String nickName) {
        try {
            User user = userRepository.findByNickName(nickName).orElseThrow(() -> new Exception("유저를 찾을 수 없습니다"));

        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/
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

    @PutMapping(value = "/user/socialing/feed/{feedId}")
    public ResponseEntity<Void> updateFeed(
            @PathVariable Integer feedId,
            @RequestPart("feed") FeedDto feedDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "removeUrls", required = false) List<String> removeUrls,
            @AuthenticationPrincipal PrincipalDetails principal
    ) throws Exception {
        FeedDto existing = feedService.getFeedDetail(feedId, principal.getUser().getUserId());
        if (!existing.getWriterId().equals(principal.getUser().getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        feedService.updateFeed(feedId, feedDto, images, removeUrls);
        return ResponseEntity.noContent().build();
    }
}
