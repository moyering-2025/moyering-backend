package com.dev.moyering.host.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dev.moyering.classring.dto.InquiryResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.dto.InquirySearchRequestDto;

public interface InquiryService {
	PageResponseDto<InquiryDto> getInquiryListByClassId(Integer classId, int page, int size) throws Exception;
	Integer writeInquriy(InquiryDto dto) throws Exception;
	List<InquiryDto> selectInquiryByHostId(Integer hostId)throws Exception;
	void replyInquiry(Integer inquiryId,Integer hostId,String iqResContent)throws Exception;
	Page<InquiryDto> searchInquiries(InquirySearchRequestDto dto) throws Exception;
	PageResponseDto<InquiryResponseDto> getMyInquiries(UtilSearchDto dto) throws Exception;
}
