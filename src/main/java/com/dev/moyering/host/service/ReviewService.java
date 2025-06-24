package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ReviewDto;

public interface ReviewService {
	List<ReviewDto> getReviewByHostId(Integer hostId);
}
