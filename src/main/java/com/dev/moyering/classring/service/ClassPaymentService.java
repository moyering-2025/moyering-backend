package com.dev.moyering.classring.service;

import com.dev.moyering.classring.dto.ClassPaymentResponseDto;
import com.dev.moyering.classring.dto.PaymentApproveRequestDto;
import com.dev.moyering.classring.dto.PaymentInitRequestDto;
import com.dev.moyering.classring.dto.UserPaymentHistoryDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.user.entity.User;

public interface ClassPaymentService {
	ClassPaymentResponseDto getClassPaymentInfo(Integer userId, Integer classId, Integer selectedCalendarId) throws Exception;
	void approvePayment(PaymentApproveRequestDto dto, User user) throws Exception;
	void initPayment(PaymentInitRequestDto dto, User user) throws Exception;
	PageResponseDto<UserPaymentHistoryDto> getUserPaymentHistory(UtilSearchDto dto) throws Exception;
	void cancelClass(Integer paymentId, Integer userId) throws Exception;
}
