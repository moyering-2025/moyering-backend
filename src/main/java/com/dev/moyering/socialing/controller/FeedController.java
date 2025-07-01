package com.dev.moyering.socialing.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.config.jwt.JwtUtil;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.socialing.service.FeedService;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
            @RequestHeader(value = "Authorization",required = false) String header
    ) {
        System.out.println("==== CONTROLLER /socialing/feeds ====");
        System.out.println("▶▶▶ Authorization header = " + header);

//        try {
//            Integer userId = principal != null ?
//                    principal.getUser().getUserId() : null;
//            List<FeedDto> feeds = feedService.getFeeds(sort, userId);
//            return new ResponseEntity<>(feeds, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        try {
//            Integer userId = jwtUtil.extractUserIdFromHeader(header); // ✅ 로그인한 경우만 값 나옴
//            System.out.println("▶▶▶ extracted userId = " + userId);
//            List<FeedDto> feeds = feedService.getFeeds(sort, userId);
//            return new ResponseEntity<>(feeds, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        Integer userId = jwtUtil.extractUserIdFromHeader(header);
        System.out.println("userId = " + userId);

        List<FeedDto> feeds = null;
        try {
            feeds = feedService.getFeeds(sort, userId);
            System.out.println("feeds.size = " + feeds.size());
            return new ResponseEntity<>(feeds, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PatchMapping(value = "/user/socialing/feed/{feedId}")
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
    public ResponseEntity<Map<String ,Object>> getMyFeeds(@RequestParam Integer userId ) {
        try{
            Map<String, Object> feedsByUserId = feedService.getFeedsByUserId(userId);
            return new ResponseEntity<>(feedsByUserId, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/socialing/popular")
    public ResponseEntity<List<FeedDto>> getPopularFeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        List<FeedDto> popularFeeds = null;
        try {
            popularFeeds = feedService.getPopularFeeds(page, size);
            return new ResponseEntity<>(popularFeeds, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}