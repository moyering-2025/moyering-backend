package com.dev.moyering.user.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.common.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import jdk.jfr.Timestamp;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
//import com.dev.moyering.gathering.entity.QGathering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	QUser user = QUser.user;

	// 관리자페이지 회원관리 내 키워드 검색 + 가입기간 필터 + 사용자 구분(전체, 일반, 강사) 필터 조회
	@Override
	public List<AdminMemberDto> searchMembers(AdminMemberSearchCond cond, Pageable pageable) throws Exception {
		return jpaQueryFactory
				.select(Projections.constructor(AdminMemberDto.class,
						user.userId, // 회원 번호
						user.userType,
						user.id, // 로그인 아이디
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
						betweenDate(cond.getFrom(), cond.getTo())
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	public Long countMembers (AdminMemberSearchCond cond) {
		return jpaQueryFactory
				.select(user.count())
				.from(user)
				.where( // 조건 객체 넣기
						likeUsername(cond.getKeyword()),
						eqUserType(cond.getUserType()),
						betweenDate(cond.getFrom(), cond.getTo())
				)
				.fetchOne();
	}

	// 관리자용 쿼리 조건 메서드 (키워드, 회원구분, 가입기간)
	private BooleanExpression likeUsername(String keyword) {  // 키워드
		return (keyword == null || keyword.isEmpty()) ? null :user.id.containsIgnoreCase(keyword);
	}
	private BooleanExpression eqUserType(String userType) {
		return (userType == null || userType.isEmpty()) ? null : user.userType.eq(userType);
	}

	private BooleanExpression betweenDate(Date from, Date to) {
		if (from == null || to == null) return null;

		// Date → LocalDateTime으로 변환
		LocalDateTime start = new Timestamp(from.getTime()).toLocalDateTime();
		LocalDateTime end = new Timestamp(to.getTime()).toLocalDateTime().plusDays(1);

		return user.regDate.between(from, to);
	}
}
