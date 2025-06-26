//package com.dev.moyering.common.repository;
//
//import java.sql.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.data.domain.PageRequest;
//
//import com.dev.moyering.common.dto.AlarmDto;
//import com.dev.moyering.common.entity.Alarm;
//import com.dev.moyering.common.entity.QAlarm;
//import com.dev.moyering.user.entity.QUser;
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.querydsl.jpa.impl.JPAUpdateClause;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class AlarmRepositoryImpl implements AlarmRepositoryCustom {
//	private final JPAQueryFactory jpaQueryFactory;
//	@Override
//	public List<AlarmDto> findAlarmListByReceiverUserId(PageRequest pageRequest, Map<String, Object> params) throws Exception {
//	    QAlarm alarm = QAlarm.alarm;
//	    QUser sender = QUser.user;
//	    
//	    BooleanExpression[] conditions = createAlarmConditions(params);
//	    
//	    List<Alarm> alarmList = jpaQueryFactory
//	        .selectFrom(alarm)
//	        .from(alarm)
//	        .where(conditions)
//	        .orderBy(alarm.alarmId.desc())
//	        .offset(pageRequest.getOffset())
//	        .limit(pageRequest.getPageSize())
//	        .fetch();
//	    return alarmList.stream()
//	    	    .map(Alarm::toDto) 
//	    	    .collect(Collectors.toList());
//	    /* List<Tuple> tupleList = jpaQueryFactory
//            .select(alarm, sender.nickName)
//            .from(alarm)
//            .leftJoin(sender).on(alarm.sender.eq(sender))
//            .where(conditions)
//            .orderBy(alarm.alarmId.desc())
//            .offset(pageRequest.getOffset())
//            .limit(pageRequest.getPageSize())
//            .fetch();
//	    return tupleList.stream()
//	        .map(tuple -> {
//	            Alarm a = tuple.get(0, Alarm.class);
//	            
//	            AlarmDto alarmDto = AlarmDto.builder()
//	                .alarmId(a.getAlarmId())
//	                .alarmType(a.getAlarmType())
//	                .senderUserId(a.getSender().getUserId())
//	                .senderUserNickName(senderNickName)
//	                .receiverUserId(a.getReceiver().getUserId())
//	                .title(a.getTitle())
//	                .content(a.getContent())
//	                .confirm(a.getConfirm())
//	                .alarmDate(a.getAlarmDate())
//	                .build();
//	            
//	            return alarmDto;
//	        })
//	        .collect(Collectors.toList());*/
//	}
//	@Override
//	public Long countAlarmsByReceiverUserId(Map<String, Object> params) throws Exception {
//	    QAlarm alarm = QAlarm.alarm;
//	    
//	    // 공통 조건 생성 (동일한 조건 사용)
//	    BooleanExpression[] conditions = createAlarmConditions(params);
//	    
//	    return jpaQueryFactory
//	        .select(alarm.count())
//	        .from(alarm)
//	        .where(conditions)
//	        .fetchOne();
//	}
//	
//	private BooleanExpression[] createAlarmConditions(Map<String, Object> params) {
//	    QAlarm alarm = QAlarm.alarm;
//	    // 파라미터 추출
//	    Integer loginId = (Integer) params.get("loginId");
//	    Integer alarmType = (Integer) params.get("alarmType");
//	    Date startDate = (Date) params.get("startDate");
//	    Date endDate = (Date) params.get("endDate");
//	    Boolean isConfirmed = (Boolean) params.get("isConfirmed");
//	    
//	    return new BooleanExpression[] {
//	        alarm.receiver.userId.eq(loginId), // 필수 조건: 수신자 = 로그인 사용자
//	        alarmType != null ? alarm.alarmType.eq(alarmType) : null, // 알람 타입 조건
//	        // 날짜 범위 조건
//	        startDate != null && endDate != null ? alarm.alarmDate.between(startDate, endDate) :
//	        startDate != null ? alarm.alarmDate.goe(startDate) :
//	        endDate != null ? alarm.alarmDate.loe(endDate) : null,
//	        isConfirmed != null ? alarm.confirm.eq(isConfirmed) : null // 확인 여부 조건
//	    };
//	}
//	@Override
//	public void updateFcmToken(Integer userId, String fcmToken) throws Exception {
//		QUser user = QUser.user;
//		JPAUpdateClause clause = jpaQueryFactory.update(user)
//				.set(user.fcmToken, fcmToken)
//				.where(user.userId.eq(userId));
//		clause.execute();
//	}
//	@Override
//	public void updateAlarmConfirm(Integer alarmId) throws Exception {
//		QAlarm alarm = QAlarm.alarm;
//		JPAUpdateClause clause = jpaQueryFactory.update(alarm)
//				.set(alarm.confirm, true)
//				.where(alarm.alarmId.eq(alarmId));
//		clause.execute();
//	}
//}
