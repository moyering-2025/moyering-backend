package com.dev.moyering.admin.repository;


import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.entity.AdminNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCouponRepository extends JpaRepository<AdminCoupon, Integer>, AdminCouponRepositoryCustom {
}
