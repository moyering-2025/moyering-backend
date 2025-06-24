package com.dev.moyering.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer>,InquiryRepositoryCustom {

}
