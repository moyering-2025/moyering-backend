package com.dev.moyering.host.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.host.entity.Inquiry;

public interface InquiryRepositoryCustom {
	Page<Inquiry> findInquiriesByClassId(Integer classId, Pageable pageable);
}
