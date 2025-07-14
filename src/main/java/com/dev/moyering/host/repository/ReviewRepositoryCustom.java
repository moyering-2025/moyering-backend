package com.dev.moyering.host.repository;

import java.util.List;

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
    
    //클래스 상세 리뷰
    List<Review> findTop3ByClassId(Integer classId)throws Exception;
    Page<Review> findReviewsByClassId(Integer hostId, Pageable pageable)throws Exception;
;
}
