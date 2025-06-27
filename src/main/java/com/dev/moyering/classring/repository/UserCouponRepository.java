package com.dev.moyering.classring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.user.entity.User;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer>, UserCouponRepositoryCustom {
	boolean existsByUserAndClassCoupon(User user, ClassCoupon classCoupon) throws Exception;
	List<UserCoupon> findAllByUser_UserIdAndClassCoupon_HostClass_ClassId(Integer userId, Integer classId) throws Exception;
}
