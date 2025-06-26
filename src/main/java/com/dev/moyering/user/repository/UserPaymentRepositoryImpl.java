package com.dev.moyering.user.repository;

import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.user.entity.QUserPayment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;


@Repository
public class UserPaymentRepositoryImpl implements UserPaymentRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;
    QUserPayment userPayment = QUserPayment.userPayment;




        @Override
        public List<AdminPaymentDto> searchPaymentList(AdminPaymentSearchCond cond, Pageable pageable) throws Exception {
            return jpaQueryFactory
                    // Projection.constructor 사용하면 dto에서 빌더 패턴 사용 안해도 됨 (추후 삭제하기)
                    .select(Projections.constructor(AdminPaymentDto.class,
                            userPayment.paymentId, // 결제 id
                            userPayment.orderNo,  // 주문번호
                            userPayment.classRegist.user.username, // 결제자 로그인 ID (studentId)
                            userPayment.classCalendar.hostClass.name, //클래스명 (className)
                            userPayment.classCalendar.hostClass.price, //클래스 금액 (totalAmount)
                            userPayment.userCoupon.adminCoupon.couponType, // 쿠폰 유형 (관리자, 호스트)
                            userPayment.userCoupon.adminCoupon.discountType, // 쿠폰할인 타입 (금액, 비율)
                            userPayment.userCoupon.adminCoupon.discount, //할인 금액 / 비율
                            userPayment.amount, // 총 결제금액
                            userPayment.paidAt, //결제일시
                            userPayment.paymentType, //결제타입 (카드, 간편결제)
                            userPayment.status // 상태 (결제완료, 취소됨)
                    ))
                    .from(userPayment)
                    .where(
                            likeOrderNoOrHostIdOrName(cond.getKeyword()),
                            eqPaymentStatus(cond.getStatus()),
                            betweenDate(cond.getFromDate(), cond.getToDate())
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        @Override
        public Long countPaymentList(AdminClassSearchCond cond) {
            return jpaQueryFactory
                    .select(userPayment.count())
                    .from(userPayment)
                    .where(
                            likeOrderNoOrHostIdOrName(cond.getKeyword()),
                            eqPaymentStatus(cond.getStatusFilter()),
                            betweenDate(cond.getFromDate(), cond.getToDate())
                    )
                    .fetchOne();
        }



        // 결제내역 검색 조건 (결제자id, 결제자 이름, 클래스명이 일치한 리스트 검색)
        private BooleanExpression likeOrderNoOrHostIdOrName(String keyword) {  // 키워드
            if (keyword == null || keyword.isEmpty()) return null; // 검색 키워드 없으면 전체 list 리턴
            return userPayment.orderNo.containsIgnoreCase(keyword)
                    .or(userPayment.classRegist.user.username.containsIgnoreCase(keyword))
                    .or(userPayment.classCalendar.hostClass.name.containsIgnoreCase(keyword));
        }

        private BooleanExpression eqPaymentStatus(String Paymentstatus) {
            return (Paymentstatus == null || Paymentstatus.isEmpty()) ? null : userPayment.status.eq(Paymentstatus);
        }

        private BooleanExpression betweenDate(Date fromDate, Date toDate) {
            if (fromDate == null || toDate == null) return null;
            //return user.regDate.between(fromDate, toDate);
            return null;
        }
    }



