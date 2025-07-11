package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.dto.SettlementAggregationDto;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;

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
    
    Page<AdminSettlement> getHostSettlementList(SettlementSearchRequestDto dto, Pageable pageable);

    /*** 특정 정산에 대한 수강생 결제 리스트*/
    List<AdminPaymentDto> getPaymentListBySettlementId(Integer settlementId);

    /*** 특정 강사에 대한 결제 리스트*/
    List<AdminSettlement> findByHostIdsettlementList(Integer hostId);

}