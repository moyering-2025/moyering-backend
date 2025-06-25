package com.dev.moyering.gathering.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
import com.dev.moyering.util.PageInfo;
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
	@Override
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
		List<GatheringInquiryDto> gatheringInquiryList = null;
		System.out.println("gatheringId : "+gatheringId);
		gatheringInquiryList = gatheringInquiryRepository.gatheringInquiryListBygatheringId(gatheringId);
		return gatheringInquiryList;
	}
	@Override
	public List<GatheringInquiryDto> findGatheringInquiriesByUserAndPeriod(PageInfo pageInfo, Map<String, Object> params) throws Exception {
		List<GatheringInquiryDto> gatheringInquiryList = null;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);

		Long cnt = gatheringInquiryRepository.selectInquiryCount(params);
		
		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
        
		gatheringInquiryList = gatheringInquiryRepository.findGatheringInquiriesByUserAndPeriod(pageRequest, params);
		return gatheringInquiryList;
	}
	@Override
	public void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {

		gatheringInquiryRepository.responseToGatheringInquiry(gatheringInquiryDto);
	}
}
