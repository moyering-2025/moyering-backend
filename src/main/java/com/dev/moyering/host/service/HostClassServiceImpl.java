package com.dev.moyering.host.service;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.common.repository.SubCategoryRepository;
import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
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
    private final HostRepository hostRepository;
    private final SubCategoryRepository subCategoryRepository;
    
    
	@Value("${iupload.path}")
	private String iuploadPath;


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
	public Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception {
		MultipartFile[] files = {hostClassDto.getImg1(),hostClassDto.getImg2(),hostClassDto.getImg3(),hostClassDto.getImg4(),hostClassDto.getImg5()
				,hostClassDto.getMaterial(), hostClassDto.getPortfolio()};
		for(MultipartFile file : files) {
			if(file!=null && !file.isEmpty()) {
				File upFile = new File(iuploadPath, file.getOriginalFilename());
				file.transferTo(upFile);
			}			
		}
		if(files[0]!=null && !files[0].isEmpty()) hostClassDto.setImgName1(files[0].getOriginalFilename());
		if(files[1]!=null && !files[1].isEmpty()) hostClassDto.setImgName2(files[1].getOriginalFilename());
		if(files[2]!=null && !files[2].isEmpty()) hostClassDto.setImgName3(files[2].getOriginalFilename());
		if(files[3]!=null && !files[3].isEmpty()) hostClassDto.setImgName4(files[3].getOriginalFilename());
		if(files[4]!=null && !files[4].isEmpty()) hostClassDto.setImgName5(files[4].getOriginalFilename());
		if(files[5]!=null && !files[5].isEmpty()) hostClassDto.setMaterialName(files[5].getOriginalFilename());
		if(files[6]!=null && !files[6].isEmpty()) hostClassDto.setPortfolioName(files[6].getOriginalFilename());
		HostClass hostClass = hostClassDto.toEntity(); 
		hostClassRepository.save(hostClass);
		
		dates.forEach(date-> { 
			ClassCalendar cc = ClassCalendar.builder()
											.startDate(date)
											.endDate(date)
											.status("검수중")
											.hostClass(HostClass.builder()
																.classId(hostClass.getClassId())
																.build())
											.build();
			
			classCalendarRepository.save(cc);
		});
		
		return hostClass.getClassId();
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
