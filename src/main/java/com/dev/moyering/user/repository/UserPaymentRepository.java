package com.dev.moyering.user.repository;

import com.dev.moyering.user.entity.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Integer>, UserPaymentRepositoryCustom {
}
