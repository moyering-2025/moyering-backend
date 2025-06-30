package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.service.AdminSettlementService;
import com.dev.moyering.admin.service.PaymentSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/settlement")
@RequiredArgsConstructor
@Slf4j // 로깅
@CrossOrigin(origins = "*")
public class AdminSettlementController {
    private final AdminSettlementService adminSettlementService;
    private final PaymentSettlementService paymentSettlementService;

    @GetMapping
    public ResponseEntity<Page<AdminSettlementDto>> getSettlementList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @PageableDefault(size = 20, sort = "settledAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("정산 목록 조회 요청 : keyword = {}, startDate = {}, endDate = {}, page = {}", keyword, startDate, endDate, pageable.getPageNumber());
        try {
            Page<AdminSettlementDto> settlements = adminSettlementService.getSettlementList(keyword, startDate, endDate, pageable);
            return ResponseEntity.ok(settlements);
        } catch (Exception e) {
            log.error("정산목록 조회실패 : {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}