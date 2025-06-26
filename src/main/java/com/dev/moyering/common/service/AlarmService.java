package com.dev.moyering.common.service;

import java.util.List;
import java.util.Map;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.util.PageInfo;
public interface AlarmService {
	List<AlarmDto> findAlarmListByReceiverUserId(PageInfo pageInfo, Map<String, Object> param) throws Exception;
	Boolean sendAlarm(AlarmDto alarmDto) throws Exception;
	Boolean confirmAlarm(Integer alarmId) throws Exception;
	Boolean confirmAlarmAll(List<Integer> alarmList)throws Exception;
	void registFcmToken(Integer userId, String fcmToken);
}
