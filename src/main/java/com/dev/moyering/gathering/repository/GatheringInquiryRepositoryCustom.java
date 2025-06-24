package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;

public interface GatheringInquiryRepositoryCustom {

	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;
	void responseToGatheringInquiry (GatheringInquiryDto gatheringInquiryDto) throws Exception;
	Long selectInquiryCount(Map<String, Object> params);
	List<GatheringInquiryDto> findGatheringInquiriesByUserAndPeriod(PageRequest pageRequest, Map<String, Object> params);
}
