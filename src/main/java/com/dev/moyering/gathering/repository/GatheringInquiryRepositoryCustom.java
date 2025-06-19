package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;

public interface GatheringInquiryRepositoryCustom {

	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;
	void responseToGatheringInquiry (GatheringInquiryDto gatheringInquiryDto) throws Exception;
}
