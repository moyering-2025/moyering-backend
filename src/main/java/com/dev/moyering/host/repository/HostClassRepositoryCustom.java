package com.dev.moyering.host.repository;

import java.beans.ExceptionListener;
import java.util.List;
import java.util.Map;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface HostClassRepositoryCustom {
    List<HostClass> findRecommendClassesForUser(User user) throws Exception;
    Map<Integer, List<ClassCalendarDto>> findHostClassWithCalendar(Integer hostId) throws Exception;
    

//     관리자 페이지 클래스 검색/조회
    List<AdminClassDto> searchClassForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception;

    // 관리자 페이지 조회 건수
    Long countClasses(AdminClassSearchCond cond) throws Exception;


    AdminClassDto findClassByClassId(Integer classId);
}



