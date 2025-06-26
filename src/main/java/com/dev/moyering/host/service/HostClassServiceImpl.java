package com.dev.moyering.host.service;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.common.repository.SubCategoryRepository;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostClassSearchRequestDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
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
		System.out.println(result + "rrr");

		List<Integer> classIds = result.stream().map(HostClass::getClassId).collect(Collectors.toList());

		Map<Integer, Date> startDateMap = classCalendarRepository.findEarliestStartDatesByClassIds(classIds);

		return result.stream().map(hostClass -> {
			HostClassDto dto = hostClass.toDto();
			dto.setStartDate(startDateMap.get(hostClass.getClassId()));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception {
		MultipartFile[] files = { hostClassDto.getImg1(), hostClassDto.getImg2(), hostClassDto.getImg3(),
				hostClassDto.getImg4(), hostClassDto.getImg5(), hostClassDto.getMaterial(),
				hostClassDto.getPortfolio() };
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				File upFile = new File(iuploadPath, file.getOriginalFilename());
				file.transferTo(upFile);
			}
		}
		if (files[0] != null && !files[0].isEmpty())
			hostClassDto.setImgName1(files[0].getOriginalFilename());
		if (files[1] != null && !files[1].isEmpty())
			hostClassDto.setImgName2(files[1].getOriginalFilename());
		if (files[2] != null && !files[2].isEmpty())
			hostClassDto.setImgName3(files[2].getOriginalFilename());
		if (files[3] != null && !files[3].isEmpty())
			hostClassDto.setImgName4(files[3].getOriginalFilename());
		if (files[4] != null && !files[4].isEmpty())
			hostClassDto.setImgName5(files[4].getOriginalFilename());
		if (files[5] != null && !files[5].isEmpty())
			hostClassDto.setMaterialName(files[5].getOriginalFilename());
		if (files[6] != null && !files[6].isEmpty())
			hostClassDto.setPortfolioName(files[6].getOriginalFilename());
		HostClass hostClass = hostClassDto.toEntity();
		hostClassRepository.save(hostClass);

		dates.forEach(date -> {
			ClassCalendar cc = ClassCalendar.builder().startDate(date).endDate(date).status("검수중")
					.hostClass(HostClass.builder().classId(hostClass.getClassId()).build()).build();

			classCalendarRepository.save(cc);
		});

		return hostClass.getClassId();
	}

	@Override
	public PageResponseDto<HostClassDto> searchClasses(ClassSearchRequestDto dto) throws Exception {
		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = QClassCalendar.classCalendar;

		BooleanBuilder builder = new BooleanBuilder();

		// 모집 중이면서~~ 오늘 이후의 일정
		builder.and(calendar.status.eq("모집중"));
		builder.and(calendar.startDate.goe(Date.valueOf(LocalDate.now())));

		// 검색 조건 필터 추가
		if (dto.getSido() != null && !dto.getSido().isBlank()) {
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
	    if (dto.getCategory2() != null) {
	        builder.and(hostClass.subCategory.subCategoryId.eq(dto.getCategory2()));
	    }
	    if (dto.getStartDate() != null) {
	        builder.and(calendar.startDate.goe(Date.valueOf(dto.getStartDate())));
	    }
	    if (dto.getEndDate() != null) {
	        builder.and(calendar.startDate.loe(Date.valueOf(dto.getEndDate())));
	    }
	    if (dto.getPriceMin() != null) {
	        builder.and(hostClass.price.goe(dto.getPriceMin()));
	    }
	    if (dto.getPriceMax() != null) {
	        builder.and(hostClass.price.loe(dto.getPriceMax()));
	    }
		if (dto.getName() != null) {
			 builder.and(hostClass.name.contains(dto.getName()));
		}
	    // 가장 빠른 모집중 일정 1건만 JOIN된 클래스만 조회
	    List<HostClass> result = jpaQueryFactory
	            .select(calendar.hostClass)
	            .from(calendar)
	            .join(calendar.hostClass, hostClass)
	            .where(builder)
	            .groupBy(hostClass.classId)
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


		// DTO로 변환
		List<HostClassDto> dtoList = result.stream().map(h -> {
			HostClassDto dto2 = h.toDto();
			dto2.setStartDate(startDateMap.get(h.getClassId()));
			return dto2;
		}).collect(Collectors.toList());

		return PageResponseDto.<HostClassDto>builder().content(dtoList).currentPage(dto.getPage() + 1)
				.totalPages((int) Math.ceil((double) total / dto.getSize())).totalElements(total).build();
	}

	@Override
	public Map<Integer, List<ClassCalendarDto>> getHostClassesWithCalendars(Integer hostId) throws Exception {
		try {
			Map<Integer, List<ClassCalendarDto>> hostClasses = hostClassRepository.findHostClassWithCalendar(hostId);
			for (Map.Entry<Integer, List<ClassCalendarDto>> entry : hostClasses.entrySet()) {
				List<ClassCalendarDto> calendars = entry.getValue();
			}
			return hostClasses;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	@Override
	public List<HostClassDto> selectHostClassByHostId(Integer hostId) throws Exception {
	    // hostID에 맞는 hostClass정보 list에 담기
	    List<HostClass> classListEntity = hostClassRepository.findByHostHostId(hostId);
	    List<HostClassDto> classList = new ArrayList<>();
	    for (HostClass hostClass : classListEntity) {
	        classList.add(hostClass.toDto());
	    }

	    // classList에 있는 hostClass의 id들 추출 후 classIds에 저장
	    Set<Integer> classIds = classList.stream()
	            .map(HostClassDto::getClassId)
	            .collect(Collectors.toSet());

	    // classId가 일치하는 일정들을 List에 담기
	    List<ClassCalendar> calendarEntityList = classCalendarRepository.findByHostClassClassIdIn(classIds);
	    List<ClassCalendarDto> calendarList = new ArrayList<>();
	    for (ClassCalendar calendar : calendarEntityList) {
	        calendarList.add(calendar.toDto());
	    }

	    // 일정에 있는 시작날짜와 상태를 담을 List 생성
	    List<HostClassDto> result = new ArrayList<>();

	    for (HostClassDto hostClass : classList) {
	        // 해당 클래스에 맞는 일정을 필터링
	        List<ClassCalendarDto> matchingCalendars = calendarList.stream()
	                .filter(calendar -> calendar.getClassId().equals(hostClass.getClassId()))
	                .collect(Collectors.toList());

	        // 일정이 여러 개 있을 경우, 각 일정을 가진 클래스 정보를 중복해서 생성
	        for (ClassCalendarDto calendar : matchingCalendars) {
	            // 새로운 HostClassDto 객체 생성
	            HostClassDto classWithCalendar = new HostClassDto();
	            BeanUtils.copyProperties(hostClass, classWithCalendar); // 객체 복사

	            // 일정 정보 추가
	            classWithCalendar.setStartDate(calendar.getStartDate());
	            classWithCalendar.setStatus(calendar.getStatus());
	            classWithCalendar.setCalendarId(calendar.getCalendarId());

	            result.add(classWithCalendar); // 중복된 클래스를 여러 번 추가
	        }
	    }

	    return result; // 여러 일정을 포함한 클래스 리스트 반환
	}
	
	@Override
	public HostPageResponseDto<HostClassDto> selectHostClassByHostIdWithPagination(HostClassSearchRequestDto dto) throws Exception {
	    Integer hostId = dto.getHostId();
	    int page = dto.getPage();
	    int size = dto.getSize();

	    List<HostClass> classListEntity = hostClassRepository.findByHostHostId(hostId);
	    List<HostClassDto> classList = classListEntity.stream()
	        .map(HostClass::toDto)
	        .collect(Collectors.toList());

	    Set<Integer> classIds = classList.stream()
	        .map(HostClassDto::getClassId)
	        .collect(Collectors.toSet());

	    List<ClassCalendar> calendarEntityList = classCalendarRepository.findByHostClassClassIdIn(classIds);
	    List<ClassCalendarDto> calendarList = calendarEntityList.stream()
	        .map(ClassCalendar::toDto)
	        .collect(Collectors.toList());

	    List<HostClassDto> mergedList = new ArrayList<>();

	    for (HostClassDto hostClass : classList) {
	        List<ClassCalendarDto> matchingCalendars = calendarList.stream()
	            .filter(calendar -> calendar.getClassId().equals(hostClass.getClassId()))
	            .collect(Collectors.toList());

	        for (ClassCalendarDto calendar : matchingCalendars) {
	            HostClassDto classWithCalendar = new HostClassDto();
	            BeanUtils.copyProperties(hostClass, classWithCalendar);
	            classWithCalendar.setStartDate(calendar.getStartDate());
	            classWithCalendar.setStatus(calendar.getStatus());
	            classWithCalendar.setCalendarId(calendar.getCalendarId());
	            mergedList.add(classWithCalendar);
	        }
	    }

	    // 🔍 필터링 조건 처리
	    Stream<HostClassDto> stream = mergedList.stream();

	    if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
	        stream = stream.filter(c -> c.getName() != null && c.getName().contains(dto.getKeyword()));
	    }

	    if (dto.getCategory1() != null) {
	        stream = stream.filter(c -> c.getCategory1() != null && c.getCategory1().equals(dto.getCategory1()));
	    }

	    if (dto.getCategory2() != null) {
	        stream = stream.filter(c -> c.getCategory2() != null && c.getCategory2().equals(dto.getCategory2()));
	    }

	    if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
	        stream = stream.filter(c -> c.getStatus() != null && c.getStatus().equals(dto.getStatus()));
	    }

	    if (dto.getStartDate() != null && !dto.getStartDate().isEmpty() &&
	    	    dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {

	    	    LocalDate start = LocalDate.parse(dto.getStartDate());
	    	    LocalDate end = LocalDate.parse(dto.getStartDate());

	    	    stream = stream.filter(c -> {
	    	        if (c.getStartDate() == null) return false;
	    	        LocalDate classStart = c.getStartDate().toLocalDate();
	    	        return !classStart.isBefore(start) && !classStart.isAfter(end);
	    	    });
	    	}

	    List<HostClassDto> filteredList = stream.collect(Collectors.toList());

	    // 📦 페이지네이션 적용
	    int total = filteredList.size();
	    int start = (page - 1) * size;
	    int end = Math.min(start + size, total);
	    List<HostClassDto> pageList = (start < end) ? filteredList.subList(start, end) : new ArrayList<>();

	    // 페이지 정보 계산
	    PageInfo pageInfo = new PageInfo();
	    pageInfo.setCurPage(page);
	    int allPage = (int) Math.ceil((double) total / size);
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(Math.max(1, page - 2));
	    pageInfo.setEndPage(Math.min(allPage, page + 2));
	    
	    return HostPageResponseDto.<HostClassDto>builder()
	    	    .content(pageList)
	    	    .pageInfo(pageInfo)
	    	    .build();
	}
	
	

	@Override
	public HostClassDto getClassDetail(Integer classId, Integer calendarId, Integer hostId) {
		 // 1. 클래스 조회 (host 검증 포함)
        HostClass hostClass = hostClassRepository.findByClassIdAndHost_HostId(classId, hostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 클래스에 접근할 수 없습니다."));

        // 2. 일정 조회 (classId 일치 여부 확인)
        ClassCalendar calendar = classCalendarRepository.findByCalendarIdAndHostClass_ClassId(calendarId, classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 정보가 클래스와 일치하지 않습니다."));

     // 3. HostClass → HostClassDto
        HostClassDto dto = hostClass.toDto();

        // 4. 일정 정보 추가 주입
        dto.setCalendarId(calendar.getCalendarId());
        dto.setStartDate(calendar.getStartDate());
        dto.setStatus(calendar.getStatus());

        return dto;
	}

	@Override
	public HostClassDto getClassDetailByClassID(Integer classId) throws Exception {
		HostClassDto hostclass= hostClassRepository.findById(classId).orElseThrow(()-> new Exception("해당 클래스가 존재하지 않습니다.")).toDto();
		return hostclass;
	}

//	@Override
//	public Integer saveTempClass(HostClassDto hostClassDto, List<Date> dates) throws Exception {
//		MultipartFile[] files = { hostClassDto.getImg1(), hostClassDto.getImg2(), hostClassDto.getImg3(),
//				hostClassDto.getImg4(), hostClassDto.getImg5(), hostClassDto.getMaterial(),
//				hostClassDto.getPortfolio() };
//		for (MultipartFile file : files) {
//			if (file != null && !file.isEmpty()) {
//				File upFile = new File(iuploadPath, file.getOriginalFilename());
//				file.transferTo(upFile);
//			}
//		}
//		if (files[0] != null && !files[0].isEmpty())
//			hostClassDto.setImgName1(files[0].getOriginalFilename());
//		if (files[1] != null && !files[1].isEmpty())
//			hostClassDto.setImgName2(files[1].getOriginalFilename());
//		if (files[2] != null && !files[2].isEmpty())
//			hostClassDto.setImgName3(files[2].getOriginalFilename());
//		if (files[3] != null && !files[3].isEmpty())
//			hostClassDto.setImgName4(files[3].getOriginalFilename());
//		if (files[4] != null && !files[4].isEmpty())
//			hostClassDto.setImgName5(files[4].getOriginalFilename());
//		if (files[5] != null && !files[5].isEmpty())
//			hostClassDto.setMaterialName(files[5].getOriginalFilename());
//		if (files[6] != null && !files[6].isEmpty())
//			hostClassDto.setPortfolioName(files[6].getOriginalFilename());
//		HostClass hostClass = hostClassDto.toEntity();
//		hostClassRepository.save(hostClass);
//
//		dates.forEach(date -> {
//			ClassCalendar cc = ClassCalendar.builder().startDate(date).endDate(date).status("임시저장")
//					.hostClass(HostClass.builder().classId(hostClass.getClassId()).build()).build();
//
//			classCalendarRepository.save(cc);
//		});
//
//		return hostClass.getClassId();
//	}

	

	



}

