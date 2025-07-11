package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminPaymentDto;
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
import java.util.List;
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
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /*** 정산처리*/
    @PutMapping("/{settlementId}/complete")
    public ResponseEntity<Map<String, Object>> completeSettlementSimple(
            @PathVariable Integer settlementId,
            @RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 프론트에서 보낸 계산된 금액 추출
            Integer totalSettlementAmount = (Integer) requestData.get("totalSettlementAmount");
            // 서비스에서 계산된 금액 전달
            boolean result = adminSettlementService.completeSettlement(settlementId, totalSettlementAmount);
            if (result) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }

        /*** 스케줄러 수동 실행(테스트용) */
        @PostMapping("/process-manual")
        public ResponseEntity<Map<String, Object>> processManual() { // 수정됨
            Map<String, Object> result = new HashMap<>();
            try {
                settlementSchedulerService.processAutoSettlement();
                return ResponseEntity.ok(result);
            } catch (Exception e) { // catch 블록 추가
                return ResponseEntity.status(500).body(result);
            }
        }

        /*** 스케줄러 상태 확인 (정산 대상 클래스 수등)*/
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

    @GetMapping("/{settlementId}/payments")
    public ResponseEntity<List<AdminPaymentDto>> getPaymentsBySettlementId(@PathVariable Integer settlementId) {
        log.info("정산 결제 내역 조회 요청 - settlementId: {}", settlementId);

        try {
            List<AdminPaymentDto> payments = adminSettlementService.getPaymentListBySettlementId(settlementId);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}