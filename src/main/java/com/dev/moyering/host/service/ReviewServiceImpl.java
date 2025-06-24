package com.dev.moyering.host.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.ReviewDto;
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

}
