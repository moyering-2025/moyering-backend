package com.dev.moyering.host.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.InquiryRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final InquiryRepository inquiryRepository;
	private final UserRepository userRepository;
	private final ClassCalendarRepository calendarRepository;
	
	@Override
	public PageResponseDto<InquiryDto> getInquiryListByClassId(Integer classId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Inquiry> inquiryPage = inquiryRepository.findInquiriesByClassId(classId,pageable);
		
		List<InquiryDto> dtoList = inquiryPage.getContent().stream()
				.map(Inquiry::toDto)
				.collect(Collectors.toList());
		
		return PageResponseDto.<InquiryDto>builder()
				.content(dtoList)
				.currentPage(inquiryPage.getNumber()+1)
				.totalPages(inquiryPage.getTotalPages())
				.totalElements(inquiryPage.getTotalElements())
				.build();
	}
	@Override
	public Integer writeInquriy(InquiryDto dto) throws Exception {
		User user = userRepository.findById(dto.getUserId())
	            .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
	    ClassCalendar calendar = calendarRepository.findById(dto.getCalendarId())
	            .orElseThrow(() -> new Exception("캘린더를 찾을 수 없습니다."));
	    
	    Inquiry inquiry = Inquiry.builder()
	    		.classCalendar(calendar)
	    		.user(user)
	    		.content(dto.getContent())
	    		.inquiryDate(Date.valueOf(LocalDate.now()))
	    		.build();
	    inquiryRepository.save(inquiry);
	    
		return inquiry.getInquiryId();
	}

}
