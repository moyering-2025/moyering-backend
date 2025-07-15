package com.dev.moyering.user.repository;

import static com.dev.moyering.user.entity.QUser.user;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.entity.QAdminBadge;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.classring.repository.UserCouponRepository;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.QUser;
import com.dev.moyering.user.entity.QUserBadge;
import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import static com.querydsl.core.types.Projections.constructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	private final UserCouponRepository userCouponRepository;

	// 관리자페이지 회원관리 내 키워드 검색 + 가입기간 필터 + 사용자 구분(전체, 일반, 강사) 필터 조회
	@Override
	public List<AdminMemberDto> searchMembers(AdminMemberSearchCond cond, Pageable pageable) {
		return jpaQueryFactory
				.select(Projections.constructor(AdminMemberDto.class,
						user.userId, // 회원 번호
						user.userType,
						user.username, // 로그인 아이디
						user.name,
						user.email,
						user.tel,
						user.regDate,
						user.useYn
				))
				.from(user)
				.where(
						likeUsername(cond.getKeyword()),
						eqUserType(cond.getUserType()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	public Long countMembers(AdminMemberSearchCond cond) {
		return jpaQueryFactory
				.select(user.count())
				.from(user)
				.where( // 조건 객체 넣기
						likeUsername(cond.getKeyword()),
						eqUserType(cond.getUserType()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
				.fetchOne();
	}

	@Override
	public UserDto findUserDtoByNickName(String nickName) {
		QUser user = QUser.user;
		QUserBadge userBadge = QUserBadge.userBadge;

		return jpaQueryFactory
				.select(Projections.bean(UserDto.class,
						user.userId.as("userId"),
						user.username,
						user.nickName,
						user.profile,
						userBadge.badge_img.as("badgeImg")
				))
				.from(user)
				.leftJoin(userBadge).on(userBadge.user.eq(user)
						.and(userBadge.isRepresentative.isTrue()))
				.where(user.nickName.eq(nickName))
				.fetchOne();
	}


	// 관리자용 쿼리 조건 메서드 (키워드, 회원구분, 가입기간)
	private BooleanExpression likeUsername(String keyword) {  // 키워드
		return (keyword == null || keyword.isEmpty()) ? null : user.username.containsIgnoreCase(keyword);
	}
	private BooleanExpression eqUserType(String userType) {
		return (userType == null || userType.isEmpty()) ? null : user.userType.eq(userType);
	}
	private BooleanExpression betweenDate(Date fromDate, Date toDate) {
		if (fromDate == null || toDate == null) return null;
		//return user.regDate.between(fromDate, toDate);
		return null;
	}

	
}