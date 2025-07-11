package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.host.entity.HostClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminClassDetailRepository extends JpaRepository<HostClass, Integer>, AdminClassDetailRepositoryCustom {
}
