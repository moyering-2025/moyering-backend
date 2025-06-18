package com.dev.moyering.gathering.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
@Service
public class GatheringInquiryServiceImpl implements GatheringInquiryService {
	@Autowired
	GatheringInquiryRepository gatheringInquiryRepository;
	@Override
	public Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) {
		return null;
	}
}
