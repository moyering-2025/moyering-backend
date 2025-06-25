package com.dev.moyering.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassCoupon;

public interface ClassCouponRepository extends JpaRepository<ClassCoupon, Integer>, ClassCouponRepositoryCustom {

}
