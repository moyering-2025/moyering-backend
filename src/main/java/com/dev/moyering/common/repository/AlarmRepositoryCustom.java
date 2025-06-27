package com.dev.moyering.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.common.dto.AlarmDto;

public interface AlarmRepositoryCustom {
	List<AlarmDto> findAlarmListByReceiverUserId(PageRequest pageRequest, Map<String, Object> params) throws Exception;
	Long countAlarmsByReceiverUserId(Map<String, Object> params) throws Exception;
	void updateFcmToken(Integer userId, String fcmToken) throws Exception;
	void updateAlarmConfirm(Integer alarmId) throws Exception;
}
