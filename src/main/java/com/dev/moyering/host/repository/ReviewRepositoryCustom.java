package com.dev.moyering.host.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.Review;

public interface ReviewRepositoryCustom {
    Page<Review> findReviewsByHostId(Integer hostId, Pageable pageable);
    Page<Review> getReviewsForHost(ReviewSearchRequestDto requestDto,Pageable pageable);

    Page<WritableReviewResponseDto> findWritableReviews(UtilSearchDto dto, Pageable pageable) throws Exception;
    Page<UserReviewResponseDto> findDoneReviews(UtilSearchDto dto, Pageable pageable) throws Exception;
}
