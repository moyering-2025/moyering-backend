package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>,ReviewRepositoryCustom {
	List<Review> findTop3ByHost_HostIdOrderByReviewDateDesc(Integer hostId);
	List<Review> findByClassCalendarCalendarIdIn(List<Integer> calendarIdList);
	List<Review> findByHostHostId(Integer hostId);

}
