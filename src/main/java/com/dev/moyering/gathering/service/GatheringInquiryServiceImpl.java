package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
@Service
public class GatheringInquiryServiceImpl implements GatheringInquiryService {
	@Autowired
	GatheringInquiryRepository gatheringInquiryRepository;
	@Override
	public Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		System.out.println(gatheringInquiryDto);

		GatheringInquiry gatheringInquiry = gatheringInquiryDto.toEntity();
		gatheringInquiryRepository.save(gatheringInquiry);
		return gatheringInquiry.getInquiryId();
	}
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
		List<GatheringInquiryDto> gatheringInquiryList = null;
		System.out.println("gatheringId : "+gatheringId);
		gatheringInquiryList = gatheringInquiryRepository.gatheringInquiryListBygatheringId(gatheringId);
		return gatheringInquiryList;
	}
	public void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		gatheringInquiryRepository.responseToGatheringInquiry(gatheringInquiryDto);
	}
}
