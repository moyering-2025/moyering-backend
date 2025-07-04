package com.dev.moyering.host.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;

public interface ReviewService {
	List<ReviewDto> getReviewByHostId(Integer hostId);
	PageResponseDto<ReviewDto> getAllReviewByHostId(Integer hostId, Integer page, Integer size);
	PageResponseDto<ReviewDto> getReviewsForHost(Integer hostId,int page,int size)throws Exception;
	Page<ReviewDto> searchReviews(ReviewSearchRequestDto dto)throws Exception;
	List<ReviewDto> getReviews(Integer hostId)throws Exception;
	void replyReview (Integer reviewId,Integer hostId,String revRegContent) throws Exception;
	PageResponseDto<WritableReviewResponseDto> getWritableReviews(UtilSearchDto dto) throws Exception;
	PageResponseDto<UserReviewResponseDto> getDoneReviews(UtilSearchDto dto) throws Exception;
	Integer writeReview(ReviewDto reviewDto) throws Exception;
}
