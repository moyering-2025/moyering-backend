package com.dev.moyering.common.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.entity.Alarm;

public interface AlarmRepositoryCustom {
	void updateFcmToken(Integer userId, String fcmToken) throws Exception;
	void updateAlarmConfirm(Integer alarmId) throws Exception;
	List<AlarmDto> findAlarmListByReceiverUserId(PageRequest pageRequest, Integer loginId, Integer alarmType,
			Date startDate, Date endDate, Boolean isConfirmed) throws Exception;
	Long countAlarmsByReceiverUserId(Integer loginId, Integer alarmType, Date startDate, Date endDate,
			Boolean isConfirmed) throws Exception;
	
}
