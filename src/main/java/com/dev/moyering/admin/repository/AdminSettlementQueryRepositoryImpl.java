package com.dev.moyering.admin.repository;


import com.dev.moyering.admin.dto.PaymentSettlementViewDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminSettlementQueryRepositoryImpl implements AdminSettlementQueryRepositoryCustom {
//    private final JPAQueryFactory queryFactory;
// VIEW 테이블이라, DSL이 아닌 jdbc native 쿼리 사용
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PaymentSettlementViewDto> getPendingSettlements(String searchKeyword,
                                                                LocalDate startDate,
                                                                LocalDate endDate,
                                                                Pageable pageable) {

        // 먼저 뷰 테이블 전체 데이터 확인
        try {
            Long totalViewCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM payment_settlement_view", Long.class);
            log.info("=== 디버깅 시작 ===");
            log.info("payment_settlement_view 전체 데이터 수: {}", totalViewCount);
        } catch (Exception e) {
            log.error("뷰 테이블 조회 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
        // 동적 SQL 생성
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // 기본 SELECT 쿼리
        sql.append("SELECT payment_id, student_id, payer_username, ");
        sql.append("host_id, class_id, class_name, class_price, class_date, ");
        sql.append("payment_amount, payment_type, coupon_type, ");
        sql.append("platform_fee, settlement_amount, payment_status, class_status ");
        sql.append("FROM payment_settlement_view ");
        sql.append("WHERE payment_status = ? ");
        sql.append("AND class_status = ? ");
        sql.append("AND class_date <= ? ");
        sql.append("AND payment_id NOT IN (");
        sql.append("  SELECT DISTINCT payment_id ");
        sql.append("  FROM settlement ");
        sql.append("  WHERE payment_id IS NOT NULL");
        sql.append(") ");

        // 기본 파라미터 추가
        params.add("결제완료");
        params.add("종료됨");
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        params.add(LocalDate.now().minusDays(7));


        // 검색 조건 추가
        if (StringUtils.hasText(searchKeyword)) {
            sql.append("AND (payer_username LIKE ? OR class_name LIKE ? OR CAST(host_id AS CHAR) LIKE ?) ");
            String likePattern = "%" + searchKeyword + "%";
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
        }

        // 날짜 범위 조건 추가
        if (startDate != null && endDate != null) {
            sql.append("AND class_date BETWEEN ? AND ? ");
            params.add(startDate);
            params.add(endDate);
        } else if (startDate != null) {
            sql.append("AND class_date >= ? ");
            params.add(startDate);
        } else if (endDate != null) {
            sql.append("AND class_date <= ? ");
            params.add(endDate);
        }

        // 정렬 및 페이징
        sql.append("ORDER BY class_date DESC ");
        sql.append("LIMIT ? OFFSET ? ");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        // 디버깅 로그
        log.info("실행할 SQL: {}", sql.toString());
        log.info("파라미터: {}", params);
        log.info("7일 전 날짜: {}", sevenDaysAgo);

        return jdbcTemplate.query(sql.toString(), params.toArray(), new PaymentSettlementViewRowMapper());
    }

    @Override
    public Long getPendingSettlementsCount(String searchKeyword,
                                           LocalDate startDate,
                                           LocalDate endDate) {

        // 동적 SQL 생성 (COUNT 버전)
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // 기본 COUNT 쿼리
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM payment_settlement_view ");
        sql.append("WHERE payment_status = ? ");
        sql.append("AND class_status = ? ");
        sql.append("AND class_date <= ? ");
        sql.append("AND payment_id NOT IN (");
        sql.append("  SELECT DISTINCT payment_id ");
        sql.append("  FROM settlement ");
        sql.append("  WHERE payment_id IS NOT NULL");
        sql.append(") ");

        // 기본 파라미터 추가
        params.add("결제완료");
        params.add("종료됨");
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        params.add(sevenDaysAgo);

        // 검색 조건 추가
        if (StringUtils.hasText(searchKeyword)) {
            sql.append("AND (payer_username LIKE ? OR class_name LIKE ? OR CAST(host_id AS CHAR) LIKE ?) ");
            String likePattern = "%" + searchKeyword + "%";
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
        }

        // 날짜 범위 조건 추가
        if (startDate != null && endDate != null) {
            sql.append("AND class_date BETWEEN ? AND ? ");
            params.add(startDate);
            params.add(endDate);
        } else if (startDate != null) {
            sql.append("AND class_date >= ? ");
            params.add(startDate);
        } else if (endDate != null) {
            sql.append("AND class_date <= ? ");
            params.add(endDate);
        }

        Long count = jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public PaymentSettlementViewDto getSettlementDetailByPaymentId(Long paymentId) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT payment_id, student_id, payer_username, ");
        sql.append("host_id, class_id, class_name, class_price, class_date, ");
        sql.append("payment_amount, payment_type, coupon_type, ");
        sql.append("platform_fee, settlement_amount, payment_status, class_status ");
        sql.append("FROM payment_settlement_view ");
        sql.append("WHERE payment_id = ?");

        List<PaymentSettlementViewDto> results = jdbcTemplate.query(
                sql.toString(),
                new PaymentSettlementViewRowMapper(),
                paymentId
        );

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * DB 결과를 DTO로 변환하는 RowMapper
     */
    private static class PaymentSettlementViewRowMapper implements RowMapper<PaymentSettlementViewDto> {

        @Override
        public PaymentSettlementViewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PaymentSettlementViewDto(
                    rs.getLong("payment_id"),
                    rs.getLong("student_id"),
                    rs.getString("payer_username"),
                    rs.getLong("host_id"),
                    rs.getLong("class_id"),
                    rs.getString("class_name"),
                    rs.getBigDecimal("class_price"),
                    rs.getDate("class_date").toLocalDate(),
                    rs.getBigDecimal("payment_amount"),
                    rs.getString("payment_type"),
                    rs.getString("coupon_type"),
                    rs.getBigDecimal("platform_fee"),
                    rs.getBigDecimal("settlement_amount"),
                    rs.getString("payment_status"),
                    rs.getString("class_status")
            );
        }
    }
}