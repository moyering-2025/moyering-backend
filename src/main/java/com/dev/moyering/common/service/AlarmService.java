package com.dev.moyering.common.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.util.PageInfo;
public interface AlarmService {
	Boolean sendAlarm(AlarmDto alarmDto) throws Exception;
	Boolean confirmAlarm(Integer alarmId) throws Exception;
	Boolean confirmAlarmAll(List<Integer> alarmList)throws Exception;
	void registFcmToken(Integer userId, String fcmToken)throws Exception;
	Long countAlarmsByReceiverUserId(Integer loginId, Integer alarmType, Date startDate, Date endDate, Boolean isConfirmed) throws Exception;
	List<AlarmDto> findAlarmListByReceiverUserId(PageInfo pageInfo, Integer loginId, Integer alarmType, Date startDate,
			Date endDate, Boolean isConfirmed) throws Exception;
	List<AlarmDto> getAlarmList(Integer loginId) throws Exception;
}
