package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;

public interface GatheringInquiryRepositoryCustom {

	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;

	void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;

	List<GatheringInquiryDto> findInquiriesSentByUser(Integer userId,
            Date startDate,
            Date endDate,
            Boolean isAnswered,
            PageRequest pageRequest) throws Exception;

	Long countInquiriesSentByUser(Integer userId,
			Date startDate,
			Date endDate,
	Boolean isAnswered) throws Exception;

	Long countInquiriesReceivedByOrganizer(Integer userId, Date startDate, Date endDate, Boolean isAnswered);

	List<GatheringInquiryDto> findInquiriesReceivedByOrganizer(Integer userId, Date startDate, Date endDate,
			Boolean isAnswered, PageRequest pageRequest);
}
