//package com.dev.moyering.socialing.controller;
//
//import com.dev.moyering.config.jwt.JwtUtil;
//import com.dev.moyering.socialing.dto.HostFeedDto;
//import com.dev.moyering.socialing.service.HostFeedService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class HostFeedController {
//
//    private final HostFeedService hostFeedService;
//    private final JwtUtil jwtUtil;
//
//    @GetMapping("/{hostId}")
//    public ResponseEntity<List<HostFeedDto>> getHostFeeds(
//            @PathVariable Integer hostId,
//            @RequestParam(defaultValue = "0") int offset,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestHeader(value = "Authorization", required = false) String header
//    ) throws Exception {
//
//        Integer userId = jwtUtil.extractUserIdFromHeader(header);  // 토큰 없으면 null 반환
//        System.out.println("▶▶▶ extracted userId = " + userId);
//
//        List<HostFeedDto> feeds = hostFeedService.getHostFeeds(hostId, offset, size);
//        return ResponseEntity.ok(feeds);
//    }
//
//    /**
//     * 피드 상세 조회
//     */
//    @GetMapping("/detail/{feedId}")
//    public ResponseEntity<HostFeedDto> getHostFeedDetail(@PathVariable Integer feedId) throws Exception {
//        HostFeedDto feedDetail = hostFeedService.getHostFeedDetail(feedId);
//        return ResponseEntity.ok(feedDetail);
//    }
//}
