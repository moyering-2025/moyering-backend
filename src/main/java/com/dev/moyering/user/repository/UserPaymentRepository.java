package com.dev.moyering.user.repository;

import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.UserPayment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Integer>, UserPaymentRepositoryCustom {
	boolean existsByOrderNo(String orderNo) throws Exception;
	Optional<UserPayment> findByOrderNo(String orderNo) throws Exception;
}
