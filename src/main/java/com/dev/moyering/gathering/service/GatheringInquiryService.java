package com.dev.moyering.gathering.service;

import java.util.List;
import java.util.Map;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.util.PageInfo;

public interface GatheringInquiryService {

	Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;
	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;
	List<GatheringInquiryDto> findGatheringInquiriesByUserAndPeriod(PageInfo pageInfo, Map<String, Object> param) throws Exception;
	void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;

}