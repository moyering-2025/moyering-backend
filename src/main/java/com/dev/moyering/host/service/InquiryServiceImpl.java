package com.dev.moyering.host.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final InquiryRepository inquiryRepository;
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

}
