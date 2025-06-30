package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.VisitorLogsDto;
import com.dev.moyering.admin.service.AdminVisitorLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class AdminVisitorLogsController {
//
//    private final AdminVisitorLogsService visitorLogsService; // 인터페이스로 주입!
//
//    /**
//     * 메인 페이지 (방문 기록)
//     */
//    @GetMapping("/")
//    public ResponseEntity<String> home(HttpServletRequest request) {
//        // 방문 기록
////        visitorLogsService.recordVisit(request);
//        return ResponseEntity.ok("메인 페이지입니다!");
//    }

//    /**
//     * 대시보드용 - 오늘 방문자 통계
//     */
//    @GetMapping("/visitor-stats")
//    public ResponseEntity<VisitorLogsDto> getTodayStats() {
//        VisitorLogsDto stats = visitorLogsService.getTodayStats();
//        return ResponseEntity.ok(stats);
//    }
//
//    /**
//     * 대시보드용 - 이번 달 방문자 수
//     */
//    @GetMapping("/monthly-visitors")
//    public ResponseEntity<Long> getMonthlyVisitors() {
//        long count = visitorLogsService.getMonthlyVisitorCount();
//        return ResponseEntity.ok(count);
//    }
//}