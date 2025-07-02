package com.dev.moyering.user.repository;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.user.entity.QUserPayment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.List;

import static com.dev.moyering.admin.entity.QAdminCoupon.adminCoupon;
import static com.dev.moyering.classring.entity.QUserCoupon.userCoupon;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QClassRegist.classRegist;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserPaymentRepositoryImpl implements UserPaymentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QUserPayment userPayment = QUserPayment.userPayment;

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
                        Expressions.constant(0), // calculatedDiscountAmount 초기값
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
                .innerJoin(userPayment.classRegist, classRegist)
                .innerJoin(classRegist.user, user)
                .innerJoin(userPayment.classCalendar, classCalendar)
                .innerJoin(classCalendar.hostClass, hostClass)
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
}