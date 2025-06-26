package com.dev.moyering.host.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.QClassCoupon;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassCouponRepositoryImpl implements ClassCouponRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public List<ClassCoupon> findAllByClassId(Integer classId) throws Exception {
		QClassCoupon classCoupon = QClassCoupon.classCoupon;
		
		//활성 또는 소진인 상태의 쿠폰을 가져오는데! classId를 기준으로 가져오기
		List<ClassCoupon> ccs = jpaQueryFactory
				.selectFrom(classCoupon)
				.where(classCoupon.status.ne("만료"),
						classCoupon.hostClass.classId.eq(classId)
						)
				.fetch();
		return ccs;
	}

	
}
