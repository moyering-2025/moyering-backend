package com.dev.moyering.host.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.InquiryResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.host.dto.InquirySearchRequestDto;
import com.dev.moyering.host.entity.Inquiry;

public interface InquiryRepositoryCustom {
	Page<Inquiry> findInquiriesByClassId(Integer classId, Pageable pageable) throws Exception;
	Page<Inquiry> searchInquiries(InquirySearchRequestDto dto, Pageable pageable) throws Exception;
	Page<InquiryResponseDto> findInquriesByUserId(UtilSearchDto dto, Pageable pageable) throws Exception;
}
