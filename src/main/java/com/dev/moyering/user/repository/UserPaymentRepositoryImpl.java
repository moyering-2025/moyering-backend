package com.dev.moyering.user.repository;

import static com.dev.moyering.admin.entity.QAdminCoupon.adminCoupon;
import static com.dev.moyering.host.entity.QClassCoupon.classCoupon;
import static com.dev.moyering.classring.entity.QUserCoupon.userCoupon;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QClassRegist.classRegist;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;
import static com.dev.moyering.user.entity.QUserPayment.userPayment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.classring.dto.UserPaymentHistoryDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.user.entity.QUserPayment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserPaymentRepositoryImpl implements UserPaymentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    /*** 관리자 페이지 > 결제 내역 조회 */
    // JPA는 연관관계를 기준으로 경로를 따라가야 SQL을 제대로 만들 수 있음 => **중간 경로**가 빠져버리면 Hibernate는 무슨 객체인지 몰라서 아예 데이터를 못 가져옴 !
    @Override
    public Page<AdminPaymentDto> searchPaymentList(AdminPaymentSearchCond cond, Pageable pageable) {
        List<AdminPaymentDto> content =  jpaQueryFactory
                .select(Projections.constructor(AdminPaymentDto.class,
                        userPayment.paymentId, // 결제 id
                        userPayment.orderNo,  // 주문번호
                        user.username, // 결제자 로그인 ID (studentId)
                        hostClass.name, //클래스명 (className)
                        hostClass.price, //클래스 금액 (classAmount)
                        adminCoupon.couponType, // 쿠폰 유형 (관리자, 호스트)
                        adminCoupon.discountType, // 쿠폰할인 타입 (금액, 비율)
                        adminCoupon.discount, //할인 금액 / 비율
                        Expressions.constant(0), // calculatedDiscountAmount 초기값 (금액/비율에 따른 쿠폰 적용금액)
                        userPayment.amount, // 총 결제금액 (totalAmount)
                        userPayment.paidAt, //결제일시 (LocalDateTime)
                        userPayment.paymentType, //결제타입 (카드, 간편결제)
                        userPayment.status // 상태 (결제완료, 취소됨)
                ))
                .from(userPayment)
                .leftJoin(userPayment.classRegist, classRegist)
                .leftJoin(classRegist.user, user)
                .leftJoin(userPayment.classCalendar, classCalendar)
                .leftJoin(classCalendar.hostClass, hostClass)
                .leftJoin(userPayment.userCoupon, userCoupon)
                .leftJoin(userCoupon.adminCoupon, adminCoupon)
                .where(buildSearchConditions(cond))
                .orderBy(userPayment.paidAt.desc())  // 결제 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = countPaymentList(cond);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Long countPaymentList(AdminPaymentSearchCond cond) {
        return jpaQueryFactory
                .select(userPayment.count())
                .from(userPayment)
                .leftJoin(userPayment.classRegist, classRegist)
                .leftJoin(classRegist.user, user)
                .leftJoin(userPayment.classCalendar, classCalendar)
                .leftJoin(classCalendar.hostClass, hostClass)
                .leftJoin(userPayment.userCoupon, userCoupon)
                .leftJoin(userCoupon.adminCoupon, adminCoupon)
                .where(buildSearchConditions(cond))
                .fetchOne();
    }

    /*** 검색 조건 빌더*/
    private BooleanExpression[] buildSearchConditions(AdminPaymentSearchCond cond) {
        return new BooleanExpression[]{
                containsKeyword(cond.getKeyword()),
                eqPaymentStatus(cond.getStatus()),
                payDateBetween(cond)
        };
    }

    /*** 키워드 검색 - 주문번호, 수강생ID, 클래스명 포함 검색*/
    private BooleanExpression containsKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null; // 키워드 없으면 전체 조회
        }

        String trimmedKeyword = keyword.trim();
        return userPayment.orderNo.containsIgnoreCase(trimmedKeyword)
                .or(user.username.containsIgnoreCase(trimmedKeyword))
                .or(hostClass.name.containsIgnoreCase(trimmedKeyword));
    }

    /***결제 상태 필터링*/
    private BooleanExpression eqPaymentStatus(String paymentStatus) {
        return StringUtils.hasText(paymentStatus) ?
                userPayment.status.eq(paymentStatus.trim()) : null;
    }

    private BooleanExpression payDateBetween(AdminPaymentSearchCond cond) {
        // 날짜 조건이 아예 없는 경우
        if (!cond.hasDateRange()) {
            log.debug("날짜 조건 없음");
            return null;
        }

        LocalDate fromDate = cond.getFromLocalDate();
        LocalDate toDate = cond.getToLocalDate();

        // 시작일만 있는 경우: 해당일 이후
        if (cond.hasFromDateOnly()) {
            log.debug("시작일만 설정: {}", fromDate);
            return userPayment.paidAt.goe(fromDate.atStartOfDay());
        }

        // 종료일만 있는 경우: 해당일 이전
        if (cond.hasToDateOnly()) {
            log.debug("종료일만 설정: {}", toDate);
            return userPayment.paidAt.loe(toDate.atTime(23, 59, 59));
        }

        // 시작일~종료일 모두 있는 경우: 범위 검색
        if (cond.hasFullDateRange()) {
            log.debug("날짜 범위 설정: {} ~ {}", fromDate, toDate);
            return userPayment.paidAt.between(
                    fromDate.atStartOfDay(),
                    toDate.atTime(23, 59, 59)
            );
        }

        return null;
    }

	@Override
	public Page<UserPaymentHistoryDto> findUserPaymentHistory(UtilSearchDto dto, Pageable pageable) throws Exception {
		QUserPayment p = QUserPayment.userPayment;
	    QClassRegist r = QClassRegist.classRegist;
	    QClassCalendar c = QClassCalendar.classCalendar;
	    QHostClass h = QHostClass.hostClass;
	    
	    BooleanBuilder builder = new BooleanBuilder();
	    builder.and(r.user.userId.eq(dto.getUserId()));
	    
	    String tab = dto.getTab();

	    if ("ing".equals(tab)) {
	        builder.and(p.status.eq("결제완료"))
	           .and(c.isNotNull())
	               .and(c.status.in("모집중", "모집마감"));
	    } else if ("fin".equals(tab)) {
	        builder.and(p.status.eq("결제완료"))
	           .and(c.isNotNull())
	               .and(c.status.eq("종료"));
	    } else if ("cancled".equals(tab)) {
	        builder.and(p.status.eq("취소됨"));
	    } else if ("closed".equals(tab)) {
	        builder.and(p.status.eq("결제완료"))
	           .and(c.isNotNull())
            .and(c.status.in("폐강"));
	    }
	    if (dto.getKeywords() != null && !dto.getKeywords().isBlank()) {
	        builder.and(h.name.containsIgnoreCase(dto.getKeywords()));
	    }

	    List<UserPaymentHistoryDto> content = jpaQueryFactory
	        .select(Projections.constructor(
	            UserPaymentHistoryDto.class,
	            p.paymentId,
	            h.name,
	            c.status,
	            c.startDate,
	            h.scheduleStart,
	            h.scheduleEnd,
	            h.locName,
	            h.addr,
	            p.amount,
	            h.img1,
	            p.status,
	            h.material,
	            c.calendarId,
	            h.recruitMin,
	            h.recruitMax,
	            c.registeredCount,
	            h.classId,
	            p.orderNo,
	            p.paidAt,
	            p.paymentType,
	            p.canceledAt,
	            p.couponType,
	            p.discountType,
	            p.userCoupon.ucId,
	            p.status,
	            p.classPrice
	            ))
	        .from(p)
	        .join(p.classRegist, r)
	        .join(p.classCalendar, c)
	        .join(c.hostClass, h)
	        .where(builder)
	        .offset(pageable.getOffset())
	        .limit(pageable.getPageSize())
	        .orderBy(p.paymentId.desc())
	        .fetch();

	    long total = jpaQueryFactory
	        .select(p.count())
	        .from(p)
	        .join(p.classRegist, r)
	        .leftJoin(p.classCalendar, c) // ← 이거 빠지면 NullPointerException 혹은 count 오류
	        .where(builder)
	        .fetchOne();

	    return new PageImpl<>(content, pageable, total);
	   }
}
