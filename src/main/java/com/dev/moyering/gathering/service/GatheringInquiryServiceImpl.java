package com.dev.moyering.gathering.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.util.PageInfo;
@Service
public class GatheringInquiryServiceImpl implements GatheringInquiryService {
	@Autowired
	GatheringInquiryRepository gatheringInquiryRepository;
	@Autowired
	GatheringRepository gatheringRepository;
	
	@Autowired
	private AlarmService alarmService;
	@Override
	public Integer writeGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		GatheringInquiry gatheringInquiry = gatheringInquiryDto.toEntity();
		gatheringInquiryRepository.save(gatheringInquiry);

		Gathering gathering = gatheringRepository.findById(gatheringInquiryDto.getGatheringId()).get();
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(3)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("문의가 등록 안내") // 필수 사항
				.receiverId(gathering.getUser().getUserId())
				.senderId(gatheringInquiryDto.getUserId())
				.senderNickname(gatheringInquiryDto.getNickName())
				.content(gathering.getTitle()+"에 대한 문의가 등록되었어요.")//알림 내용
				.build();
		alarmService.sendAlarm(alarmDto);
		return gatheringInquiry.getInquiryId();
	}
	@Override
	public GatheringInquiryDto findByInquiryId(Integer inquiryId) throws Exception {
		GatheringInquiry gatheringInquiry = gatheringInquiryRepository.findById(inquiryId).orElseThrow(()->new Exception("아이디 오류"));
		return gatheringInquiry.toDto();
	}
	@Override
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
		List<GatheringInquiryDto> gatheringInquiryList = null;
		gatheringInquiryList = gatheringInquiryRepository.gatheringInquiryListBygatheringId(gatheringId);
		return gatheringInquiryList;
	}
	
	@Override
	public void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		gatheringInquiryRepository.responseToGatheringInquiry(gatheringInquiryDto);
		Gathering gathering = gatheringRepository.findById(gatheringInquiryDto.getGatheringId()).get();
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(3)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("문의 답변 등록 안내") // 필수 사항
				.receiverId(gatheringInquiryDto.getUserId())
				.senderId(gathering.getUser().getUserId())
				.senderNickname(gathering.getUser().getNickName())
				.content(gathering.getTitle()+"에 등록하신 문의에 대한 답변이 등록되었어요.")//알림 내용
				.build();
		
		alarmService.sendAlarm(alarmDto);
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

	    System.out.println("startDate: " + startDate);
	    System.out.println("endDate: " + endDate);
	    System.out.println("isAnswered: " + isAnswered);
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
