package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;

public interface GatheringInquiryRepositoryCustom {

	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;
	void responseToGatheringInquiry (GatheringInquiryDto gatheringInquiryDto) throws Exception;
	long countInquiriesReceivedByOrganizer(Integer organizerUserId, Date startDate, Date endDate, Boolean isAnswered);
	List<GatheringInquiryDto> findInquiriesReceivedByOrganizer(Integer organizerUserId, Date startDate, Date endDate,
			Boolean isAnswered, PageRequest pageRequest);
	long countInquiriesSentByUser(Integer userId, Date startDate, Date endDate, Boolean isAnswered);
	List<GatheringInquiryDto> findInquiriesSentByUser(Integer userId, Date startDate, Date endDate, Boolean isAnswered,
			PageRequest pageRequest);
	
}
