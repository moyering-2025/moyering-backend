package com.dev.moyering.classring.service;

import com.dev.moyering.classring.dto.ClassPaymentResponseDto;

public interface ClassPaymentService {
	ClassPaymentResponseDto getClassPaymentInfo(Integer userId, Integer classId, Integer selectedCalendarId) throws Exception;

}
