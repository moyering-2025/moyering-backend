package com.dev.moyering.host.service;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;

public interface InquiryService {
	PageResponseDto<InquiryDto> getInquiryListByClassId(Integer classId, int page, int size);

}
