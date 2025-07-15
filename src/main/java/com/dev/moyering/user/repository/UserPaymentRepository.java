package com.dev.moyering.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.user.entity.UserPayment;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Integer>, UserPaymentRepositoryCustom {
	boolean existsByOrderNo(String orderNo) throws Exception;
	Optional<UserPayment> findByOrderNo(String orderNo) throws Exception;
	List<UserPayment> findAllByClassCalendar_CalendarId(Integer calendarId)throws Exception;
}
