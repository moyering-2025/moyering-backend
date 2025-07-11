package com.dev.moyering.admin.repository;

import static com.dev.moyering.admin.entity.QAdminCoupon.adminCoupon;
import static com.dev.moyering.admin.entity.QAdminSettlement.adminSettlement;
import static com.dev.moyering.classring.entity.QUserCoupon.userCoupon;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QClassRegist.classRegist;
import static com.dev.moyering.host.entity.QHost.host;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;
import static com.dev.moyering.user.entity.QUserPayment.userPayment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminSettlementDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.entity.QAdminSettlement;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

//private String username; // 강사 로그인 아이디
//private String hostName; // 강사 이름 (user.name)
//private String className; // 클래스명


@Slf4j
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
                        userPayment.amount.sum(),
                        userPayment.platformFee.sum(),
//                        userPayment.amount.calcula


//                    private Integer totalIncome; // 강의 당 총 순수 입금액
//                    private Integer totalDiscount; // 강의 당 총 할인 금액
//                    private Integer totalPlatformFee; // 총 플랫폼 수수료
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
                .leftJoin(hostClass).on(host.hostId.eq(hostClass.host.hostId))
                .leftJoin(user).on(host.userId.eq(user.userId))
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

        String trimmedKeyword = searchKeyword.trim();
        return adminSettlement.hostId.stringValue().contains(trimmedKeyword)
                .or(adminSettlement.classCalendar.hostClass.name.containsIgnoreCase(trimmedKeyword))
                .or(user.username.containsIgnoreCase(trimmedKeyword))  // 강사 아이디 검색 추가
                .or(user.name.containsIgnoreCase(trimmedKeyword));     // 강사 이름 검색도 추가
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

    @Override
    public Page<AdminSettlement> getHostSettlementList(SettlementSearchRequestDto dto, Pageable pageable) {
        QAdminSettlement settlement = QAdminSettlement.adminSettlement;
        QHost host = QHost.host;
        QClassCalendar calendar = QClassCalendar.classCalendar;
        QHostClass hostClass = QHostClass.hostClass;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(host.hostId.eq(dto.getHostId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (dto.getStartDate() != null && !dto.getStartDate().isBlank()) {
            builder.and(settlement.settlementDate.goe(LocalDate.parse(dto.getStartDate(), formatter)));
        }

        if (dto.getEndDate() != null && !dto.getEndDate().isBlank()) {
            builder.and(settlement.settlementDate.loe(LocalDate.parse(dto.getEndDate(), formatter)));
        }

        List<AdminSettlement> content = queryFactory.selectFrom(settlement)
                .join(settlement.classCalendar, calendar)
                .join(calendar.hostClass, hostClass)
                .join(hostClass.host, host).where(builder)
                .orderBy(settlement.settlementDate.desc()).offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(settlement)
                .join(settlement.classCalendar, calendar)
                .join(calendar.hostClass, hostClass)
                .join(hostClass.host, host).where(builder).fetchCount();


        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<AdminPaymentDto> getPaymentListBySettlementId(Integer settlementId) {
        log.info("정산별 결제 내역 조회 시작 - settlementId: {}", settlementId);

        // 1단계: settlementId로 calendarId 조회
        Integer calendarId = queryFactory
                .select(adminSettlement.classCalendar.calendarId)
                .from(adminSettlement)
                .where(adminSettlement.settlementId.eq(settlementId))
                .fetchOne();

        if (calendarId == null) {
            log.warn("정산 ID {}에 해당하는 calendarId를 찾을 수 없습니다.", settlementId);
            return Collections.emptyList(); // 빈 리스트 반환
        }

        log.debug("정산 ID {} → calendarId {} 조회 완료", settlementId, calendarId);

        // 2단계: calendarId로 해당 클래스의 결제 내역 조회
        List<AdminPaymentDto> content = queryFactory
                .select(Projections.constructor(AdminPaymentDto.class,
                        userPayment.paymentId, // 결제 id
                        userPayment.orderNo,  // 주문번호
                        user.username, // 결제자 로그인 ID (studentId)
                        hostClass.name, //클래스명 (className)
                        hostClass.price, //클래스 금액 (classAmount)
                        adminCoupon.couponType, // 쿠폰 유형 (관리자, 호스트)
                        adminCoupon.discountType, // 쿠폰할인 타입 (금액, 비율)
                        adminCoupon.discount, //할인 금액 / 비율
                        Expressions.constant(0), // calculatedDiscountAmount 초기값
                        userPayment.amount, // 총 결제금액 (totalAmount)
                        userPayment.paidAt, //결제일시 (LocalDateTime)
                        userPayment.paymentType, //결제타입 (카드, 간편결제)
                        userPayment.status // 상태 (결제완료, 취소됨)
                ))
                .from(userPayment)
                .join(userPayment.classRegist, classRegist)
                .join(classRegist.user, user)
                .join(userPayment.classCalendar, classCalendar)
                .join(classCalendar.hostClass, hostClass)
                .leftJoin(userPayment.userCoupon, userCoupon)
                .leftJoin(userCoupon.adminCoupon, adminCoupon)
                .where(classCalendar.calendarId.eq(calendarId)) // 핵심: calendarId로 필터링
                .orderBy(userPayment.paidAt.desc())  // 결제 최신순 정렬
                .fetch();

        log.info("정산 ID {} (calendarId: {})의 결제 내역 {}건 조회 완료",
                settlementId, calendarId, content.size());

        return content;
    }

	@Override
	public List<AdminSettlement> findByHostIdsettlementList(Integer hostId) {
		QAdminSettlement settlement = QAdminSettlement.adminSettlement;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;
		QHost host = QHost.host;
		
		BooleanBuilder builder= new BooleanBuilder();
		builder.and(host.hostId.eq(hostId));
		
		List<AdminSettlement> list = queryFactory.selectFrom(settlement)
				.leftJoin(settlement.classCalendar,calendar)
				.leftJoin(calendar.hostClass,hostClass)
				.leftJoin(hostClass.host,host)
				.where(builder).fetch();
		
		return list;
	}
}
