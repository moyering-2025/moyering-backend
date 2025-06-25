package com.dev.moyering.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.moyering.admin.entity.AdminCoupon;

@Repository
public interface AdminCouponRepository extends JpaRepository<AdminCoupon, Integer>, AdminCouponRepositoryCustom {
    boolean existsByCouponCode(String couponCode);
    
}
