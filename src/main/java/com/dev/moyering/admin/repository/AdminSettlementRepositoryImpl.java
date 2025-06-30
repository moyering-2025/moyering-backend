package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.service.AdminSettlementService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dev.moyering.admin.entity.QAdminSettlement.adminSettlement;


// 강의 결제를 했고, 결제상태인 "payment"가 "결제완료"이고,
// hostClass의 상태가 "종료됨" 이면 => 정산


@Repository
@RequiredArgsConstructor
public class AdminSettlementRepositoryImpl implements AdminSettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AdminSettlementDto> getSettlementListBySettlementId(Integer settlementId) {

        // 데이터 단건 조회
        AdminSettlementDto content = queryFactory
                .select(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.hostId,
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        adminSettlement.bankAccount,
                        adminSettlement.totalIncome,
                        adminSettlement.platformFee,
                        adminSettlement.settlementAmount,
                        adminSettlement.userPayment.paymentId,
                        adminSettlement.classCalendar.calendarId))
                .from(adminSettlement)
                .where(adminSettlement.settlementId.eq(settlementId))
                .fetchOne();
        return Optional.ofNullable(content);
    }

    // 검색조건에 따른 데이터 리스트 조회
    @Override
    public List<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return queryFactory
                .select(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.hostId,
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        adminSettlement.bankAccount,
                        adminSettlement.totalIncome,
                        adminSettlement.platformFee,
                        adminSettlement.settlementAmount,
                        adminSettlement.userPayment.paymentId,
                        adminSettlement.classCalendar.calendarId))
                .from(adminSettlement)
                .where(
                        searchSettlement(searchKeyword), // 검색 조건
                        settlementDateBetween(startDate, endDate) // 정산기간 조건
                )
                .orderBy(adminSettlement.settledAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    @Override
    public Long getSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate) {
        Long count = queryFactory
                .select(adminSettlement.settlementId.count())
                .from(adminSettlement)
                .where(
                        searchSettlement(searchKeyword),
                        settlementDateBetween(startDate, endDate)
                )
                .fetchOne();
        return count != null ? count : 0L;
    }

    // 검색 조건 메서드
    // StringUtils : 자바의 String 클래스가 제공하는 문자열 관련 기능을 강화한 클래스
    // 파라미터 값을 null을 주더라도 에러 발생 X
    private BooleanExpression searchSettlement(String searchKeyword) {
        if (!StringUtils.hasText(searchKeyword)) {
            return null; // 검색어 없으면 전체 조회
        } // 검색어가 있으면 (강사 아이디), 클래스명
        return adminSettlement.hostId.stringValue().contains(searchKeyword)
                .or(adminSettlement.classCalendar.hostClass.name.containsIgnoreCase(searchKeyword));

    }

    // 정산날짜 범위 검색
    private BooleanExpression settlementDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return adminSettlement.settlementDate.between(startDate, endDate);
        } else if (startDate != null) {
            return adminSettlement.settlementDate.goe(startDate);
        } else if (endDate != null) {
            return adminSettlement.settlementDate.loe(endDate);
        }
        return null;
    }
}
