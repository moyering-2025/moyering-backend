package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.common.dto.MainSearchRequestDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.StudentSearchRequestDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

public interface HostClassRepositoryCustom {
    List<HostClass> findRecommendClassesForUser(User user) throws Exception;
    Map<Integer, List<ClassCalendarDto>> findHostClassWithCalendar(Integer hostId) throws Exception;


    Page <AdminClassDto> searchClassForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception;
    Long countClasses(AdminClassSearchCond cond) throws Exception;
//    AdminClassDto findClassByClassId(Integer classId) throws Exception;
    Page<User> searchClassStudent(StudentSearchRequestDto dto, Pageable pageable) throws Exception;
    
    List<HostClass> findSearchClass(MainSearchRequestDto dto)throws Exception;
    
    //클래스 상세 3개 추천해주기
    List<HostClassDto> findRecommendClassesInDetail(Integer subCategoryId, Integer categoryId,Integer classId) throws Exception;

    // 관리자가 강사가 요청한 강의 승인
    int updateClassStatus(Integer classId) throws Exception;
}



