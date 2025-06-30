package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminSettlementServiceImpl implements AdminSettlementService {
    private final AdminSettlementRepository adminSettlementRepository;

    // 조건에 맞는 정산 리스트 가져오기
    @Override
    public Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception {
        try {
            log.info("정산목록 조회 - 키워드 : {}, 정산 기간 시작일 : {}, 정산 기간 종료일 : {}, 페이지 : {}", searchKeyword, startDate, endDate, pageable);

            // 전체 개수 조회
            Long totalCount = adminSettlementRepository.getSettlementListCount(searchKeyword, startDate, endDate);
            // 데이터 조회
            List<AdminSettlementDto> settlements = adminSettlementRepository.getSettlementList(searchKeyword, startDate, endDate, pageable);
            log.info("정산 목록 조회 완료 - 총 {}건, 현재 페이지 {}건", totalCount, settlements.size());
            return new PageImpl<>(settlements, pageable, totalCount);
        } catch (Exception e) {
            log.error("에러 메세지", e);
            throw new Exception("정산 조회 중 에러 발생");
        }
    }

    // 정산 상세조회
    @Override
    public AdminSettlementDto getSettlementById(Integer settlementId) throws Exception {
        try {
            log.info("정산 상세 조회 시작 - settlementId: {}", settlementId);
            if (settlementId == null || settlementId <= 0) {
                throw new IllegalArgumentException("유효하지 않은 정산 ID입니다.");
            }

            AdminSettlementDto settlement = adminSettlementRepository.getSettlementListBySettlementId(settlementId)
                    .orElseThrow(() -> new Exception("정산 정보를 찾을 수 없습니다. ID: " + settlementId));

            return settlement;

        } catch (Exception e) {
            log.error("정산 상세 조회 중 오류 발생 - settlementId: {}", settlementId, e);
            throw new Exception("정산 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}


/*** 정산 처리 (상태 변경)*/
//        @Override
//        @Transactional
//        public void processSettlement (Integer settlementId) throws Exception {
//            try {
//                log.info("정산 처리 시작 - settlementId: {}", settlementId);
//
//                // 1. 정산 정보 조회
//                AdminSettlementDto settlement = getSettlementById(settlementId);
//
//                // 2. 정산 상태 검증
//                if ("COMPLETED".equals(settlement.getSettlementStatus())) {
//                    throw new Exception("이미 완료된 정산입니다.");
//                }
//
//                if ("CANCELLED".equals(settlement.getSettlementStatus())) {
//                    throw new Exception("취소된 정산은 처리할 수 없습니다.");
//                }
//
//                // 3. 정산 금액 검증
//                if (settlement.getSettlementAmount() == null || settlement.getSettlementAmount().compareTo(BigDecimal.ZERO) <= 0) {
//                    throw new Exception("정산 금액이 유효하지 않습니다.");
//                }
//
//                // 4. 정산 처리 (실제 비즈니스 로직)
//                processSettlementPayment(settlement);
//
//                // 5. 정산 상태 업데이트
//                updateSettlementStatus(settlementId, "COMPLETED");
//
//                log.info("정산 처리 완료 - settlementId: {}, amount: {}", settlementId, settlement.getSettlementAmount());
//
//            } catch (Exception e) {
//                log.error("정산 처리 중 오류 발생 - settlementId: {}", settlementId, e);
//                throw new Exception("정산 처리 중 오류가 발생했습니다: " + e.getMessage());
//            }
//        }
//    }
//}
