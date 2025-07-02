package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.dto.SettlementAggregationDto;
import com.dev.moyering.admin.service.AdminSettlementService;
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


    /**
     * 정산 내역 목록 조회 (이미 생성된 정산 데이터)
     */
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

    // ========== 정산 대기 목록 관리 ==========

    /*** 정산 대기 목록 조회
     * 조건: 결제완료 + 클래스종료 + 미정산*/
    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingSettlements(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "classDate", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("정산 대기 목록 조회 요청 - 검색어: {}, 기간: {} ~ {}, 페이지: {}",
                searchKeyword, startDate, endDate, pageable.getPageNumber());

        try {
            Page<PaymentSettlementViewDto> pendingSettlements = adminSettlementService
                    .getPendingSettlementList(searchKeyword, startDate, endDate, pageable);

            // 성공 응답 구성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "정산 대기 목록 조회 성공");
            response.put("data", pendingSettlements.getContent());

            // 페이징 정보
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("totalElements", pendingSettlements.getTotalElements());
            pageInfo.put("totalPages", pendingSettlements.getTotalPages());
            pageInfo.put("currentPage", pendingSettlements.getNumber());
            pageInfo.put("pageSize", pendingSettlements.getSize());
            pageInfo.put("hasNext", pendingSettlements.hasNext());
            pageInfo.put("hasPrevious", pendingSettlements.hasPrevious());
            pageInfo.put("first", pendingSettlements.isFirst());
            pageInfo.put("last", pendingSettlements.isLast());

            response.put("pageInfo", pageInfo);

            log.info("정산 대기 목록 조회 성공 - 총 {}건, {}페이지",
                    pendingSettlements.getTotalElements(), pendingSettlements.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("정산 대기 목록 조회 실패", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "정산 대기 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("pageInfo", null);

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 정산 대기 목록 개수 조회
     */
    @GetMapping("/pending/count")
    public ResponseEntity<Map<String, Object>> getPendingSettlementsCount(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("정산 대기 개수 조회 요청 - 검색어: {}, 기간: {} ~ {}", searchKeyword, startDate, endDate);

        try {
            Long count = adminSettlementService.getPendingSettlementListCount(searchKeyword, startDate, endDate);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "정산 대기 개수 조회 성공");
            response.put("count", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("정산 대기 개수 조회 실패", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "정산 대기 개수 조회 중 오류가 발생했습니다: " + e.getMessage());
            errorResponse.put("count", 0);

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 특정 클래스의 정산 집계 데이터 조회
     */
    @GetMapping("/aggregation/{classId}")
    public ResponseEntity<Map<String, Object>> getSettlementAggregation(
            @PathVariable Integer classId) {

        log.info("정산 집계 데이터 조회 요청 - classId: {}", classId);

        try {
            Optional<SettlementAggregationDto> aggregation = adminSettlementService
                    .getSettlementAggregationByClass(classId.longValue());

            Map<String, Object> response = new HashMap<>();
            if (aggregation.isPresent()) {
                response.put("success", true);
                response.put("message", "정산 집계 데이터 조회 성공");
                response.put("data", aggregation.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "정산할 데이터가 없습니다");
                response.put("data", null);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("정산 집계 데이터 조회 실패: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "정산 집계 데이터 조회 중 오류가 발생했습니다");
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // ========== 향후 확장 가능한 API들 ==========

    // TODO: 정산 데이터 생성 API
    // @PostMapping("/create/{classId}")

    // TODO: 정산 완료 처리 API
    // @PutMapping("/{settlementId}/complete")

    // TODO: 정산 취소 API
    // @PutMapping("/{settlementId}/cancel")
}