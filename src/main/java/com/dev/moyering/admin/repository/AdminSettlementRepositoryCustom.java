package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.dto.SettlementAggregationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdminSettlementRepositoryCustom {

   /*** 정산 ID로 정산 내역 단건 조회*/
    Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId);

    /*** 정산 내역 목록 조회 (페이징)*/
    Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /*** 정산 내역 목록 개수 조회*/
    Long getSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate);
}