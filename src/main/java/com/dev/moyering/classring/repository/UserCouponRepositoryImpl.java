package com.dev.moyering.classring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.entity.QUserCoupon;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.entity.QClassCoupon;
import com.dev.moyering.host.entity.QHostClass;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
	@Override
	public List<UserCoupon> findAvailableCoupons(Integer userId, Integer classId) throws Exception {
		QUserCoupon userCoupon = QUserCoupon.userCoupon;
		QClassCoupon classCoupon = QClassCoupon.classCoupon;

        return jpaQueryFactory
            .selectFrom(userCoupon)
            .leftJoin(userCoupon.classCoupon, classCoupon).fetchJoin()
            .where(
                userCoupon.user.userId.eq(userId),
                userCoupon.status.eq("미사용"),
                // (관리자 쿠폰) coupon_id가 null이 아니면 포함
                userCoupon.adminCoupon.isNotNull()
                .or(
                    // (강사 쿠폰) class_coupon_id가 null이 아니고, 해당 classId와 일치하면 포함
                    userCoupon.classCoupon.isNotNull()
                    .and(userCoupon.classCoupon.hostClass.classId.eq(classId))
                )
            )
            .fetch();
	}
	@Override
	public Page<UserCoupon> findAllCouponsByUserId(Integer userId, Pageable pageable) throws Exception {
		QUserCoupon userCoupon = QUserCoupon.userCoupon;
		List<UserCoupon> content = jpaQueryFactory.selectFrom(userCoupon)
				.where(userCoupon.user.userId.eq(userId)).orderBy(userCoupon.ucId.desc())
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		Long total = jpaQueryFactory.select(userCoupon.count()).from(userCoupon)
				.where(userCoupon.user.userId.eq(userId)).fetchOne();
		return new PageImpl<UserCoupon>(content, pageable, total);	
	}
}
