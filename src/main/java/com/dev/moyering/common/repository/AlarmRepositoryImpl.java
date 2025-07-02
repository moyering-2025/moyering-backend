package com.dev.moyering.common.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.entity.Alarm;
import com.dev.moyering.common.entity.QAlarm;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public void updateFcmToken(Integer userId, String fcmToken) throws Exception {
		QUser user = QUser.user;
		JPAUpdateClause clause = jpaQueryFactory.update(user)
				.set(user.fcmToken, fcmToken)
				.where(user.userId.eq(userId));
		clause.execute();
	}
	@Override
	public void updateAlarmConfirm(Integer alarmId) throws Exception {
		QAlarm alarm = QAlarm.alarm;
		JPAUpdateClause clause = jpaQueryFactory.update(alarm)
				.set(alarm.confirm, true)
				.where(alarm.alarmId.eq(alarmId));
		clause.execute();
	}
	private BooleanExpression[] createAlarmConditions(Integer alarmType, Date startDate, Date endDate, Boolean isConfirmed) {
	    QAlarm alarm = QAlarm.alarm;
	    
	    return new BooleanExpression[] {
	        alarmType != null ? alarm.alarmType.eq(alarmType) : null,
	        startDate != null && endDate != null ? alarm.alarmDate.between(startDate, endDate) :
	        startDate != null ? alarm.alarmDate.goe(startDate) :
	        endDate != null ? alarm.alarmDate.loe(endDate) : null,
	        isConfirmed != null ? alarm.confirm.eq(isConfirmed) : null
	    };
	}
	@Override
	public List<AlarmDto> findAlarmListByReceiverUserId(PageRequest pageRequest,Integer loginId, Integer alarmType, Date startDate, Date endDate, Boolean isConfirmed) {
	    QAlarm alarm = QAlarm.alarm;

	    BooleanExpression[] conditions = createAlarmConditions(alarmType, startDate, endDate, isConfirmed);
	    
	    List<Alarm> alarmList = jpaQueryFactory
	    	    .selectFrom(alarm)
	    	    .where(alarm.receiverId.eq(loginId))
	    	    .where(conditions)
	    	    .orderBy(alarm.alarmId.desc())
	    	    .offset(pageRequest.getOffset())
	    	    .limit(pageRequest.getPageSize())
	    	    .fetch();
	    return alarmList.stream()
	    	    .map(Alarm::toDto) 
	    	    .collect(Collectors.toList());
	}
	@Override
	public Long countAlarmsByReceiverUserId(Integer loginId, Integer alarmType, Date startDate, Date endDate, Boolean isConfirmed) {
	    QAlarm alarm = QAlarm.alarm;
	    
	    // 공통 조건 생성 (동일한 조건 사용)
	    BooleanExpression[] conditions = createAlarmConditions(alarmType, startDate, endDate, isConfirmed);
	    
	    return jpaQueryFactory
	        .select(alarm.count())
	        .from(alarm)
    	    .where(alarm.receiverId.eq(loginId))
    	    .where(conditions)
	        .fetchOne();
	}
}
