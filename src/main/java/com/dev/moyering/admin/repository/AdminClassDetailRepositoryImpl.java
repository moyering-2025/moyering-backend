package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.QAdminCoupon;
import com.dev.moyering.classring.entity.QUserCoupon;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.HostClass;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminClassDetailRepositoryImpl implements AdminClassDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HostClass> findHostClassByClassId(Integer classId) {
        return List.of();
    }

    @Override
    public List<ClassCalendar> findClassCalendarByClassId(Integer classId) {
        return List.of();
    }

    @Override
    public List<ClassRegist> findClassRegistByClassId(Integer classId) {
        return List.of();
    }
}


