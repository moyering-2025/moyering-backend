package com.dev.moyering.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer>, UserCouponRepositoryCustom {
	boolean existsByUserAndClassCoupon(User user, ClassCoupon classCoupon) throws Exception;
}
