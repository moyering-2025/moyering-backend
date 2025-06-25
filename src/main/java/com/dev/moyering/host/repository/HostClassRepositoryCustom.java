package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Map;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HostClassRepositoryCustom {
    List<HostClass> findRecommendClassesForUser(User user) throws Exception;
    Map<Integer, List<ClassCalendarDto>> findHostClassWithCalendar(Integer hostId) throws Exception;


    Page<AdminClassDto> searchClassForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception;
    Long countClasses(AdminClassSearchCond cond) throws Exception;
//    AdminClassDto findClassByClassId(Integer classId) throws Exception;
}



