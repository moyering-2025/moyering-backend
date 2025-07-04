package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminSettlementDto;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static com.dev.moyering.admin.entity.QAdminSettlement.adminSettlement;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QHost.host;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;
import static com.dev.moyering.user.entity.QUserPayment.userPayment;

//private String username; // 강사 로그인 아이디
//private String hostName; // 강사 이름 (user.name)
//private String className; // 클래스명



@Repository
@RequiredArgsConstructor
public class AdminSettlementRepositoryImpl implements AdminSettlementRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId) {
        AdminSettlementDto content = queryFactory
                .selectDistinct(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        user.username, // 강사 로그인 아이디
                        user.name, // 강사 이름
                        hostClass.name, // 클래스 이름
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus, // WP, CP, RQ
                        host.bankName,
                        host.accNum,
                        userPayment.amount.sum()
                                .subtract(userPayment.platformFee.sum()),// 실제 예정 금액
                        adminSettlement.settlementAmount // 실제 정산 금액
                ))
                .from(adminSettlement)
                .leftJoin(adminSettlement.classCalendar, classCalendar)
                .leftJoin(userPayment).on(
                        userPayment.classCalendar.calendarId.eq(classCalendar.calendarId)
                                .and(userPayment.status.eq("결제완료")) // 결제완료된 건만
                )
                .join(host).on(host.hostId.eq(adminSettlement.hostId))
                .join(hostClass).on(host.hostId.eq(hostClass.host.hostId)
                        .and(hostClass.classId.eq(classCalendar.hostClass.classId)))
                .join(user).on(host.userId.eq(user.userId))
                .where(adminSettlement.settlementId.eq(settlementId))
                .groupBy(
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        user.username, // 강사 로그인 아이디
                        user.name, // 강사 이름
                        hostClass.name, // 클래스 이름
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        host.bankName,
                        host.accNum,
                        adminSettlement.settlementAmount
                )
                .fetchOne();
        return Optional.ofNullable(content);
    }

    @Override
    public Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // 1. 데이터 조회 (List로 받음)
        List<AdminSettlementDto> content = queryFactory
                .selectDistinct(Projections.constructor(AdminSettlementDto.class,
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        user.username, // 강사 로그인 아이디
                        user.name, // 강사 이름
                        hostClass.name, // 클래스 이름
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus, // PENDING, COMPLETED, CANCELLED
                        host.bankName,
                        host.accNum,
                        userPayment.amount.sum()
                                .subtract(userPayment.platformFee.sum()),// 실제 예정 금액
                        adminSettlement.settlementAmount // 실제 정산 금액
                ))
                .from(adminSettlement)
                .leftJoin(adminSettlement.classCalendar, classCalendar)
                .leftJoin(userPayment).on(
                        userPayment.classCalendar.calendarId.eq(classCalendar.calendarId)
                                .and(userPayment.status.eq("결제완료")) // 결제완료된 건만
                )
                .join(host).on(host.hostId.eq(adminSettlement.hostId))
                .join(hostClass).on(host.hostId.eq(hostClass.host.hostId)
                        .and(hostClass.classId.eq(classCalendar.hostClass.classId)))
                .join(user).on(host.userId.eq(user.userId))
                .where(
                        searchSettlement(searchKeyword),
                        settlementDateBetween(startDate, endDate)
                )
                .groupBy(
                        adminSettlement.settlementId,
                        adminSettlement.classCalendar.calendarId,
                        adminSettlement.hostId,
                        user.username, // 강사 로그인 아이디
                        user.name, // 강사 이름
                        hostClass.name, // 클래스 이름
                        adminSettlement.settlementDate,
                        adminSettlement.settledAt,
                        adminSettlement.settlementStatus,
                        host.bankName,
                        host.accNum,
                        adminSettlement.settlementAmount
                )
                .orderBy(
                        Expressions.cases()
                                .when(adminSettlement.settlementStatus.in("RQ")).then(0)
                                .when(adminSettlement.settlementStatus.in("WT")).then(1)
                                .when(adminSettlement.settlementStatus.in("CP")).then(2)
                                .otherwise(3).asc(),
                        adminSettlement.settlementDate.desc()
                )
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
                .selectDistinct(adminSettlement.settlementId.count())
                .from(adminSettlement)
                .leftJoin(adminSettlement.classCalendar, classCalendar)
                .leftJoin(host).on(host.hostId.eq(adminSettlement.hostId))
                .where(
                        searchSettlement(searchKeyword),
                        settlementDateBetween(startDate, endDate)
                )
                .fetchOne();
        return count != null ? count : 0L;
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