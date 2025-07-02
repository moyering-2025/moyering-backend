package com.dev.moyering.gathering.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
import com.dev.moyering.util.PageInfo;
@Service
public class GatheringInquiryServiceImpl implements GatheringInquiryService {
	@Autowired
	GatheringInquiryRepository gatheringInquiryRepository;
	@Override
	public Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
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
	public void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		gatheringInquiryRepository.responseToGatheringInquiry(gatheringInquiryDto);
	}
	@Override
	public Integer findInquirieCntReceivedByOrganizer(Integer loginId, Date startDate, Date endDate, Boolean isAnswered)
			throws Exception {
		Long cnt = gatheringInquiryRepository.countInquiriesReceivedByOrganizer(loginId, startDate, endDate, isAnswered);
		return cnt.intValue();
	}
	@Override
	public List<GatheringInquiryDto> findInquiriesSentByUser(PageInfo pageInfo, Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		Long cnt = gatheringInquiryRepository.countInquiriesSentByUser(loginId, startDate, endDate, isAnswered);
		
		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return gatheringInquiryRepository.findInquiriesSentByUser(loginId, startDate, endDate, isAnswered, pageRequest);
	}
	@Override
	public List<GatheringInquiryDto> findInquiriesReceivedByOrganizer (PageInfo pageInfo, Integer loginId, Date startDate, Date endDate,
			Boolean isAnswered) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		Long cnt = gatheringInquiryRepository.countInquiriesReceivedByOrganizer(loginId, startDate, endDate, isAnswered);
		System.out.println("cnt : " + cnt);
		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		return gatheringInquiryRepository.findInquiriesReceivedByOrganizer(loginId, startDate, endDate, isAnswered, pageRequest);
		
	}
	@Override
	public Integer findInquirieCntSentByUser(Integer loginId, Date startDate, Date endDate, Boolean isAnswered)
			throws Exception {
		Long cnt = gatheringInquiryRepository.countInquiriesSentByUser(loginId, startDate, endDate, isAnswered);
		return cnt.intValue();
	}
}
