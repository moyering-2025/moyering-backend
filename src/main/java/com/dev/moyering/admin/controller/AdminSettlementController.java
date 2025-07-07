package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.service.AdminSettlementService;
import com.dev.moyering.admin.service.SettlementSchedulerService;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/settlement")
@RequiredArgsConstructor
@Slf4j // 로깅
@CrossOrigin(origins = "*")
public class AdminSettlementController {
    private final AdminSettlementService adminSettlementService;
    private final SettlementSchedulerService settlementSchedulerService;

    // 스케쥴러 테스트용
    private final JPAQueryFactory queryFactory;
    private final ClassCalendarRepository classCalendarRepository;



    /*** 정산 내역 목록 조회 */
    @GetMapping
    public ResponseEntity<Page<AdminSettlementDto>> getSettlementList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "settledAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("정산 목록 조회 요청 - keyword: {}, startDate: {}, endDate: {}, page: {}",
                keyword, startDate, endDate, pageable.getPageNumber());

        try {
            Page<AdminSettlementDto> settlements = adminSettlementService.getSettlementList(keyword, startDate, endDate, pageable);
            log.info("정산 목록 조회 성공 - 총 {}건", settlements.getTotalElements());
            return ResponseEntity.ok(settlements);
        } catch (Exception e) {
            log.error("정산 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /*** 정산 내역 단건 조회*/
    @GetMapping("/{settlementId}")
    public ResponseEntity<Map<String, Object>> getSettlementDetail(
            @PathVariable Integer settlementId) {

        log.info("정산 단건 조회 요청 - settlementId: {}", settlementId);

        try {
            Optional<AdminSettlementDto> settlement = adminSettlementService.getSettlementBySettlementId(settlementId);

            Map<String, Object> response = new HashMap<>();
            if (settlement.isPresent()) {
                response.put("success", true);
                response.put("message", "정산 내역 조회 성공");
                response.put("data", settlement.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "정산 내역을 찾을 수 없습니다");
                response.put("data", null);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("정산 단건 조회 실패: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "정산 내역 조회 중 오류가 발생했습니다");
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /*** 정산처리*/
    @PutMapping("/{settlementId}/complete")
    public ResponseEntity<Map<String, Object>> completeSettlementSimple(
            @PathVariable Integer settlementId) {

        log.info("정산 완료 처리 요청 - settlementId: {}", settlementId);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = adminSettlementService.completeSettlement(settlementId);

            if (result) {
                response.put("success", true);
                response.put("message", "정산이 완료되었습니다.");
                response.put("settlementId", settlementId);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "정산 처리에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            log.error("정산 완료 처리 실패 - settlementId: {}, error: {}", settlementId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "정산 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


        /*** 스케줄러 수동 실행(테스트용) */
        @PostMapping("/process-manual")
        public ResponseEntity<Map<String, Object>> processManual() { // 수정됨
            Map<String, Object> result = new HashMap<>();

            try {
                log.info("수동 정산 처리 시작");
                settlementSchedulerService.processAutoSettlement();

                result.put("success", true);
                result.put("message", "정산처리 완료");
                result.put("executedAt", LocalDateTime.now()); // LocalDateTime으로 변경

                return ResponseEntity.ok(result);
            } catch (Exception e) { // catch 블록 추가
                log.error("수동 정산 처리 중 오류 발생", e);

                result.put("success", false);
                result.put("message", "정산 처리 실패: " + e.getMessage());
                result.put("executedAt", LocalDateTime.now());

                return ResponseEntity.status(500).body(result);
            }
        }

        /**
         * 스케줄러 상태 확인 (정산 대상 클래스 수등)
         */
        @GetMapping("/test-status")
        public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
            Map<String, Object> status = new HashMap<>();

            try {
                status.put("success", true);
                status.put("message", "스케줄러 서비스가 정상 동작 중입니다!");
                status.put("currentTime", LocalDateTime.now());
                status.put("schedulerClass", settlementSchedulerService.getClass().getSimpleName());

                return ResponseEntity.ok(status);
            } catch (Exception e) {
                log.error("상태 체크 실패", e);

                status.put("success", false);
                status.put("message", "상태 체크 실패: " + e.getMessage());

                return ResponseEntity.status(500).body(status);
            }
        }
}