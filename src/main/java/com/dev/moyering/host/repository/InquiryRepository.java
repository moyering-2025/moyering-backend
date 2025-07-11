package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer>,InquiryRepositoryCustom {
	List<Inquiry> findByClassCalendarCalendarIdIn(List<Integer> calendarIdList)throws Exception;
	Optional<Inquiry> findByHostHostId(Integer hostId)throws Exception;
}
