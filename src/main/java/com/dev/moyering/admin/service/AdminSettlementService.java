package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.dto.SettlementAggregationDto;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdminSettlementService {
    /*** 정산 ID로 단건 조회*/
    Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId);

    /*** 정산 내역 리스트 조회 (이미 생성된 정산 데이터)*/
    Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /*** 정산 내역 총 개수*/
    Long getSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate);

    boolean completeSettlement(Integer settlementId);
    
    /*** 특정강사 정산 리스트 조회 */
    Page<AdminSettlementDto> getHostSettlementList(SettlementSearchRequestDto dto);

    /*** 특정 정산의 수강생 목록 조회 */
    List <AdminPaymentDto> getPaymentListBySettlementId(Integer settlementId);

}


//    // ========== 정산 대기 목록 조회 ==========
//    /*** 정산 대기 목록 조회 (뷰 테이블에서 집계)
//     * 조건: 결제완료 + 클래스종료 + 미정산*/
//    Page<PaymentSettlementViewDto> getPendingSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable);
//
//    /*** 정산 대기 목록 총 개수*/
//    Long getPendingSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate);
//
//    /*** 특정 클래스의 정산 집계 데이터 조회
//     * 정산 데이터 생성 시 사용*/
//    Optional<SettlementAggregationDto> getSettlementAggregationByClass(Long classId);


