package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;

public interface GatheringInquiryService {

	Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception;
	List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception;

}
