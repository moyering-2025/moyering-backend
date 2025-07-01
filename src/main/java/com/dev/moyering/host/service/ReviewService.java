package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;

public interface ReviewService {
	List<ReviewDto> getReviewByHostId(Integer hostId);
	PageResponseDto<ReviewDto> getAllReviewByHostId(Integer hostId, Integer page, Integer size);
	PageResponseDto<ReviewDto> getReviewsForHost(Integer hostId,int page,int size)throws Exception;

}
