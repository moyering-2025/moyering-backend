package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.HostClass;

import java.util.List;

public interface AdminClassDetailRepositoryCustom {
    List<HostClass> findHostClassByClassId (Integer classId);
    List<ClassCalendar> findClassCalendarByClassId (Integer classId);
    List<ClassRegist> findClassRegistByClassId (Integer classId);
    // 클래스 정보 조회



    // 상세설명

    // 스케쥴
    // 수강생 목록


}
