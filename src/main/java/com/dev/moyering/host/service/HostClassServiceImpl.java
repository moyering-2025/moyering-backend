package com.dev.moyering.host.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HostClassServiceImpl implements HostClassService {
    private final HostClassRepository hostClassRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
	@Override
	public List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception {
	    List<HostClass> result;
		if (userId == null) {
	        result = hostClassRepository.findRecommendClassesForUser(null);
	    } else {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
	        result = hostClassRepository.findRecommendClassesForUser(user);
	    }
		System.out.println(result+"rrr");
		
		
		List<Integer> classIds = result.stream()
		        .map(HostClass::getClassId)
		        .collect(Collectors.toList());

	    Map<Integer, Date> startDateMap = classCalendarRepository.findEarliestStartDatesByClassIds(classIds);

        return result.stream()
        		.map(hostClass -> {
                    HostClassDto dto = hostClass.toDto();
                    dto.setStartDate(startDateMap.get(hostClass.getClassId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }


	@Override
	public void registClass(HostClassDto hostClassDto) throws Exception {
		hostClassRepository.save(hostClassDto.toEntity());
	}


	@Override
	public ClassSearchResponseDto searchClasses(ClassSearchRequestDto dto) throws Exception {
		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
		
		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		//모집 중이면서~~ 오늘 이후의 일정
		builder.and(calendar.status.eq("모집중"));
		builder.and(calendar.startDate.goe(Date.valueOf(LocalDate.now())));
		
		//검색 조건 필터 추가
		if (dto.getSido() !=null && !dto.getSido().isBlank()) {
			builder.and(hostClass.addr.contains(dto.getSido()));
		}
		if (dto.getCategory1() != null) {
		 builder.and(hostClass.subCategory.firstCategory.categoryId.eq(dto.getCategory1()));
		}
	    if (dto.getCategory2() != null) {
	        builder.and(hostClass.subCategory.subCategoryId.eq(dto.getCategory2()));
	    }
	    if (dto.getStartDate() != null) {
	        builder.and(calendar.startDate.goe(Date.valueOf(dto.getStartDate())));
	    }
	    if (dto.getEndDate() != null) {
	        builder.and(calendar.endDate.loe(Date.valueOf(dto.getEndDate())));
	    }
	    if (dto.getPriceMin() != null) {
	        builder.and(hostClass.price.goe(dto.getPriceMin()));
	    }
	    if (dto.getPriceMax() != null) {
	        builder.and(hostClass.price.loe(dto.getPriceMax()));
	    }
	    
	    // 가장 빠른 모집중 일정 1건만 JOIN된 클래스만 조회
	    List<HostClass> result = jpaQueryFactory
	            .select(calendar.hostClass)
	            .from(calendar)
	            .join(calendar.hostClass, hostClass)
	            .where(builder)
	            .groupBy(calendar.hostClass.classId)
	            .orderBy(calendar.startDate.min().asc())
	            .offset(pageable.getOffset())
	            .limit(pageable.getPageSize())
	            .fetch();
	    //총 개수 (distinct count!)
	    Long total = jpaQueryFactory
	            .select(calendar.hostClass.classId.countDistinct())
	            .from(calendar)
	            .where(builder)
	            .fetchOne();
	    
	    //날짜만 얻어오기
		List<Integer> classIds = result.stream()
		        .map(HostClass::getClassId)
		        .collect(Collectors.toList());
	    Map<Integer, Date> startDateMap = classCalendarRepository.findEarliestStartDatesByClassIds(classIds);

	    //DTO로 변환
	    List<HostClassDto> dtoList = result.stream()
	    		.map(h -> {
	                HostClassDto dto2 = h.toDto();
	                dto2.setStartDate(startDateMap.get(h.getClassId()));
	                return dto2;
	            }).collect(Collectors.toList());
	    


	    return ClassSearchResponseDto.builder()
	            .content(dtoList)
	            .currentPage(dto.getPage() + 1)
	            .totalPages((int) Math.ceil((double) total / dto.getSize()))
	            .totalElements(total)
	            .build();
	}
	
}
