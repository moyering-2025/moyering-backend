package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.config.jwt.JwtUtil;
import com.dev.moyering.socialing.dto.*;
import com.dev.moyering.socialing.service.HostFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HostFeedController {

    private final HostFeedService hostFeedService;
    private final JwtUtil jwtUtil;

    @GetMapping("/feedHost")
    public ResponseEntity<List<HostFeedDto>> getHostFeeds(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestHeader(value = "Authorization", required = false) String header
    ) throws Exception {

        Integer userId = jwtUtil.extractUserIdFromHeader(header);  // 토큰 없으면 null 반환
        System.out.println("▶▶▶ extracted userId = " + userId);

        List<HostFeedDto> feeds = hostFeedService.getHostFeeds(offset, size, category);
        return ResponseEntity.ok(feeds);
    }

    @PostMapping("/host/createFeedHost")
    public ResponseEntity<?> createHostFeed(
            @RequestPart("feed") HostFeedCreateDto feedDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Integer userId = principal.getUser().getUserId();
        try {
            HostFeedResponseDto result = hostFeedService.createHostFeed(userId, feedDto, images);
            return ResponseEntity.ok(result.getFeedId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("피드 등록 실패: " + e.getMessage());
        }
    }

    @GetMapping("/feedHost/{id}")
    public ResponseEntity<?> getHostFeedDetail(@PathVariable("id") Integer feedId) {
        try {
            HostFeedDetailDto detail = hostFeedService.getHostFeedDetail(feedId);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("피드를 찾을 수 없습니다.");
        }
    }

    @PatchMapping("/host/feedEdit/{feedId}")
    public ResponseEntity<?> updateHostFeed(
            @PathVariable Integer feedId,
            @RequestPart("dto") HostFeedUpdateDto dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam(value = "removeUrls", required = false) List<String> removeUrls
    ) {
        try {
            hostFeedService.updateHostFeed(feedId, principal.getUser().getUserId(), dto, images,removeUrls);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 실패");
        }
    }

    @DeleteMapping("/host/feedDelete/{feedId}")
    public ResponseEntity<?> deleteHostFeed(@PathVariable Integer feedId,
                                            @AuthenticationPrincipal PrincipalDetails principal) throws Exception {

        System.out.println(">>> principal: " + principal);
        Integer userId = principal.getUser().getUserId();
        hostFeedService.deleteHostFeed(feedId, userId);
        return ResponseEntity.ok("삭제 완료");
    }
}
