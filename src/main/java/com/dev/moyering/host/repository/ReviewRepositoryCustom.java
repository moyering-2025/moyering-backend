package com.dev.moyering.host.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.host.entity.Review;

public interface ReviewRepositoryCustom {
    Page<Review> findReviewsByHostId(Integer hostId, Pageable pageable);
}
