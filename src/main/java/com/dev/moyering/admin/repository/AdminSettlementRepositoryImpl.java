package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.dto.SettlementAggregationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dev.moyering.admin.entity.QAdminSettlement.adminSettlement;

@Repository
@RequiredArgsConstructor
public class AdminSettlementRepositoryImpl implements AdminSettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    // ========== 기존 정산 내역 조회 (QueryDSL 사용) ==========

    @Override
    public Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId) {
        AdminSettlementDto content = queryFactory
                .select(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        adminSettlement.bankType,
                        adminSettlement.bankAccount,
                        adminSettlement.totalIncome,
                        adminSettlement.platformFee,
                        adminSettlement.settlementAmount
                ))
                .from(adminSettlement)
                .where(adminSettlement.settlementId.eq(settlementId))
                .fetchOne();
        return Optional.ofNullable(content);
    }

    @Override
    public Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // 1. 데이터 조회 (List로 받음)
        List<AdminSettlementDto> content = queryFactory
                .select(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        adminSettlement.bankType,
                        adminSettlement.bankAccount,
                        adminSettlement.totalIncome,
                        adminSettlement.platformFee,
                        adminSettlement.settlementAmount))
                .from(adminSettlement)
                .where(
                        searchSettlement(searchKeyword),
                        settlementDateBetween(startDate, endDate)
                )
                .orderBy(adminSettlement.settledAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 총 개수 조회
        Long totalCount = getSettlementListCount(searchKeyword, startDate, endDate);

        // 3. Page 객체로 변환하여 반환
        return new PageImpl<>(content, pageable, totalCount);
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

    // ========== 정산 대기 목록 조회 (Native Query 사용) ==========

    @Override
    @SuppressWarnings("unchecked")
    public Page<PaymentSettlementViewDto> getPendingSettlementList(String searchKeyword,
                                                                   LocalDate startDate,
                                                                   LocalDate endDate,
                                                                   Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("psv.payment_id, psv.student_id, psv.payer_username, ")
                .append("psv.host_id, psv.class_id, psv.class_name, psv.class_price, ")
                .append("psv.class_date, psv.payment_amount, psv.payment_type, ")
                .append("psv.coupon_type, psv.platform_fee, psv.settlement_amount, ")
                .append("psv.payment_status, psv.class_status ")
                .append("FROM payment_settlement_view psv ")
                .append("WHERE psv.payment_status = '결제완료' ")
                .append("AND psv.class_status = '종료됨' ")
                .append("AND psv.class_date <= current_date - INTERVAL 7 DAY ")
                .append("AND psv.class_id NOT IN (")
                .append("    SELECT DISTINCT cc.class_id ")
                .append("    FROM settlement s ")
                .append("    JOIN class_calendar cc ON s.calendar_id = cc.calendar_id")
                .append("    WHERE s.settlement_status IN ('COMPLETED', 'CANCELLED')" )
                .append(") ");

        // 동적 조건 추가
        if (StringUtils.hasText(searchKeyword)) {
            sql.append("AND (CAST(psv.host_id AS CHAR) LIKE :searchKeyword ")
                    .append("OR UPPER(psv.class_name) LIKE UPPER(:searchKeyword)) ");
        }

        if (startDate != null) {
            sql.append("AND psv.class_date >= :startDate ");
        }

        if (endDate != null) {
            sql.append("AND psv.class_date <= :endDate ");
        }

        sql.append("ORDER BY psv.class_date DESC ")
                .append("LIMIT :limit OFFSET :offset");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 파라미터 설정
        if (StringUtils.hasText(searchKeyword)) {
            query.setParameter("searchKeyword", "%" + searchKeyword + "%");
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());

        List<Object[]> results = query.getResultList();

        // 1. 데이터 변환
        List<PaymentSettlementViewDto> content = results.stream()
                .map(row -> new PaymentSettlementViewDto(
                        ((Number) row[0]).longValue(),     // payment_id
                        ((Number) row[1]).longValue(),     // student_id
                        (String) row[2],                   // payer_username
                        ((Number) row[3]).longValue(),     // host_id
                        ((Number) row[4]).longValue(),     // class_id
                        (String) row[5],                   // class_name
                        (BigDecimal) row[6],               // class_price
                        ((java.sql.Date) row[7]).toLocalDate(), // class_date
                        (BigDecimal) row[8],               // payment_amount
                        (String) row[9],                   // payment_type
                        (String) row[10],                  // coupon_type
                        (BigDecimal) row[11],              // platform_fee
                        (BigDecimal) row[12],              // settlement_amount
                        (String) row[13],                  // payment_status
                        (String) row[14]                   // class_status
                ))
                .collect(Collectors.toList());

        // 2. 총 개수 조회
        Long totalCount = getPendingSettlementListCount(searchKeyword, startDate, endDate);

        // 3. Page 객체로 변환하여 반환
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Long getPendingSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT psv.class_id) ")
                .append("FROM payment_settlement_view psv ")
                .append("WHERE psv.payment_status = '결제완료' ")
                .append("AND psv.class_status = '종료됨' ")
                .append("AND psv.class_date <= current_date - INTERVAL 7 DAY ") // 종료된지 7일 이후부터 정산 list에 띄우기
                .append("AND psv.class_id NOT IN (") // 정산 내역에 있으면 안됨
                .append("    SELECT DISTINCT cc.class_id ")
                .append("    FROM settlement s ")
                .append("    JOIN class_calendar cc ON s.calendar_id = cc.calendar_id")
                .append("    WHERE s.settlement_status IN ('COMPLETED', 'CANCELLED')" )
                .append(") ");

        if (StringUtils.hasText(searchKeyword)) {
            sql.append("AND (CAST(psv.host_id AS CHAR) LIKE :searchKeyword ")
                    .append("OR UPPER(psv.class_name) LIKE UPPER(:searchKeyword)) ");
        }

        if (startDate != null) {
            sql.append("AND psv.class_date >= :startDate ");
        }

        if (endDate != null) {
            sql.append("AND psv.class_date <= :endDate ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (StringUtils.hasText(searchKeyword)) {
            query.setParameter("searchKeyword", "%" + searchKeyword + "%");
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public Optional<SettlementAggregationDto> getSettlementAggregationByClass(Long classId) {
        String sql = "SELECT " +
                "psv.host_id, " +
                "psv.class_id, " +
                "psv.class_name, " +
                "psv.class_date, " +
                "SUM(psv.payment_amount) as total_income, " +
                "SUM(psv.platform_fee) as total_platform_fee, " +
                "SUM(psv.settlement_amount) as total_settlement_amount " +
                "FROM payment_settlement_view psv " +
                "WHERE psv.class_id = :classId " +
                "AND psv.payment_status = '결제완료' " +
                "AND psv.class_status = '종료됨' " +
                "GROUP BY psv.host_id, psv.class_id, psv.class_name, psv.class_date";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("classId", classId);

        try {
            Object[] result = (Object[]) query.getSingleResult();
            SettlementAggregationDto dto = new SettlementAggregationDto(
                    ((Number) result[0]).intValue(),       // host_id -> Integer
                    ((Number) result[1]).intValue(),      // class_id -> Long
                    (String) result[2],                    // class_name
                    ((java.sql.Date) result[3]).toLocalDate(), // class_date
                    (BigDecimal) result[4],                // total_income
                    (BigDecimal) result[5],                // total_platform_fee
                    (BigDecimal) result[6]                 // total_settlement_amount
            );
            return Optional.of(dto);
        } catch (javax.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    // ========== 조건 메서드들 ==========

    private BooleanExpression searchSettlement(String searchKeyword) {
        if (!StringUtils.hasText(searchKeyword)) {
            return null;
        }
        return adminSettlement.hostId.stringValue().contains(searchKeyword)
                .or(adminSettlement.classCalendar.hostClass.name.containsIgnoreCase(searchKeyword));
    }

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