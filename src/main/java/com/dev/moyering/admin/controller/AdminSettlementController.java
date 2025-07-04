package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminSettlementDto;
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
}
