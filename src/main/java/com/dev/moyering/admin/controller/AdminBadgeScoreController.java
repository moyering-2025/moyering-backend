package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.dto.AdminBadgeScoreDto;
import com.dev.moyering.admin.service.AdminBadgeScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/badge")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminBadgeScoreController {
    private final AdminBadgeScoreService adminBadgeScoreService;

    // 배지 관리 페이지 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBadges(AdminBadgeDto adminBadgeDto) {
        try {
            // 서비스에서 dto변환 리스트 가져오기
            List<AdminBadgeDto> badges = adminBadgeScoreService.getAllBadges();
            List<AdminBadgeScoreDto> badgeScores = adminBadgeScoreService.getAllBadgeScores();

            // Map으로 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("badges", badges);
            response.put("badgeScores", badgeScores);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 활동점수 수정
    @PutMapping("/{activeScoreId}")
    public ResponseEntity<AdminBadgeScoreDto> updateBadgeScore(
            @PathVariable("activeScoreId") Integer activeScoreId,
            @Valid @RequestBody AdminBadgeScoreDto badgeScoreDto) {
        try {
            if (!activeScoreId.equals(badgeScoreDto.getActiveScoreId())) {
                return ResponseEntity.badRequest().build();
            }
            AdminBadgeScoreDto updatedScore = adminBadgeScoreService.updateBadgeScores(badgeScoreDto);
            return ResponseEntity.ok(updatedScore);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
