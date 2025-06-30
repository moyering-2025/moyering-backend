package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.repository.AdminSettlementQueryRepository;
import com.dev.moyering.admin.repository.AdminSettlementQueryRepositoryImpl;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.admin.repository.AdminSettlementRepositoryImpl;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.ClassCalendarRepositoryImpl;
import com.dev.moyering.user.repository.UserPaymentRepository;
import com.dev.moyering.user.repository.UserPaymentRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentSettlementServiceImpl implements PaymentSettlementService {

    // Native Query + JdbcTemplate 방식에서는 직접 구현체를 만들었기 때문에 Spring이 인터페이스에 대한 Bean을 자동 생성하지 않음
    // 따라서 구현체를 직접 주입받아야 함
    private final AdminSettlementQueryRepositoryImpl paymentSettlementQueryRepository;
    private final AdminSettlementRepositoryImpl adminSettlementRepository;
    private final UserPaymentRepositoryImpl userPaymentRepository;
    private final ClassCalendarRepositoryImpl classCalendarRepository;
    @Override
    public PageImpl<PaymentSettlementViewDto> getPendingSettlements(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception {
        try {
            log.info("정산 예정 목록 조회 - 키워드 : {}, 정산 예정일 기간 - 시작일: {},  정산 예정일 기간 - 종료일 : {}, 페이지 : {}", searchKeyword, startDate, endDate, pageable);
            // 전체 개수 조회
            Long totalCount = paymentSettlementQueryRepository.getPendingSettlementsCount(searchKeyword, startDate, endDate);

            // 데이터 조회
            List<PaymentSettlementViewDto> settlementsToDo = paymentSettlementQueryRepository.getPendingSettlements(searchKeyword, startDate, endDate, pageable);
            log.info("정산 목록 조회 완료 - 총 {}건, 현재 페이지 {}건", totalCount, settlementsToDo.size());
            return new PageImpl<>(settlementsToDo, pageable, totalCount);

        } catch (Exception e) {
            log.error("에러 메세지", e);
            throw new Exception("정산 할 목록 조회 중 에러 발생");
        }
    }

}
