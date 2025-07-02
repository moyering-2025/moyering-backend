package com.dev.moyering.gathering.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.util.PageInfo;

public interface GatheringInquiryService {

	Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;
	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;
	void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;
	Integer findInquirieCntReceivedByOrganizer(Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception;
	List<GatheringInquiryDto> findInquiriesReceivedByOrganizer(PageInfo pageInfo, Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception;
	Integer findInquirieCntSentByUser(Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception;
	List<GatheringInquiryDto> findInquiriesSentByUser(PageInfo pageInfo, Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception;

}