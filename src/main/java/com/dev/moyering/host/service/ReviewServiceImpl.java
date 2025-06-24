package com.dev.moyering.host.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.host.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	@Override
	public List<ReviewDto> getReviewByHostId(Integer hostId) {
		return reviewRepository.findTop3ByHost_HostIdOrderByReviewDateDesc(hostId)
				.stream().map(r->r.toDto()).collect(Collectors.toList());
	}
	@Override
	public PageResponseDto<ReviewDto> getAllReviewByHostId(Integer hostId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Review> reviewPage = reviewRepository.findReviewsByHostId(hostId, pageable);
		
		//dto로 변환
		List<ReviewDto> dtoList = reviewPage.getContent().stream()
				.map(Review::toDto)
				.collect(Collectors.toList());
		//PageResponseDto로 변환
		return PageResponseDto.<ReviewDto>builder()
				.content(dtoList)
				.currentPage(reviewPage.getNumber()+1)
				.totalPages(reviewPage.getTotalPages())
				.totalElements(reviewPage.getTotalElements())
				.build();
	}

}
