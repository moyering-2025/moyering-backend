package com.dev.moyering.host.service;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dev.moyering.admin.dto.AdminClassDetailDto;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.host.entity.Host;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.common.repository.SubCategoryRepository;
import com.dev.moyering.host.dto.CalendarUserDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostClassSearchRequestDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import com.dev.moyering.host.dto.StudentSearchRequestDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.ClassRegistRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.host.repository.InquiryRepository;
import com.dev.moyering.host.repository.ReviewRepository;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostClassServiceImpl implements HostClassService {
	private final HostClassRepository hostClassRepository;
	private final ClassCalendarRepository classCalendarRepository;
	private final UserRepository userRepository;
	private final HostRepository hostRepository;
	private final SubCategoryRepository subCategoryRepository;
	private final ClassRegistRepository classRegistRepository;
	private final InquiryRepository inquiryRepository;
	private final ReviewRepository reviewRepository;
	private final AdminSettlementRepository settlementRepository;

	// 알람서비스 선언
	private final AlarmService alarmService;
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
		hostClass.setRegDate(Date.valueOf(LocalDate.now()));
		hostClassRepository.save(hostClass);

		dates.forEach(date -> {
			ClassCalendar cc = ClassCalendar.builder().startDate(date).endDate(date).status("승인대기")
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
		List<HostClass> result = jpaQueryFactory.select(calendar.hostClass).from(calendar)
				.join(calendar.hostClass, hostClass).where(builder).groupBy(hostClass.classId)
				.orderBy(calendar.startDate.min().asc()).offset(pageable.getOffset()).limit(pageable.getPageSize())
				.fetch();
		// 총 개수 (distinct count!)
		Long total = jpaQueryFactory.select(calendar.hostClass.classId.countDistinct()).from(calendar).where(builder)
				.fetchOne();

		// 날짜만 얻어오기
		List<Integer> classIds = result.stream().map(HostClass::getClassId).collect(Collectors.toList());
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
		Set<Integer> classIds = classList.stream().map(HostClassDto::getClassId).collect(Collectors.toSet());

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
	public HostPageResponseDto<HostClassDto> selectHostClassByHostIdWithPagination(HostClassSearchRequestDto dto)
			throws Exception {
		Integer hostId = dto.getHostId();
		int page = dto.getPage();
		int size = dto.getSize();

		List<HostClass> classListEntity = hostClassRepository.findByHostHostId(hostId);
		List<HostClassDto> classList = classListEntity.stream().map(HostClass::toDto).collect(Collectors.toList());

		Set<Integer> classIds = classList.stream().map(HostClassDto::getClassId).collect(Collectors.toSet());

		List<ClassCalendar> calendarEntityList = classCalendarRepository.findByHostClassClassIdIn(classIds);
		List<ClassCalendarDto> calendarList = calendarEntityList.stream().map(ClassCalendar::toDto)
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

		if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
			stream = stream.filter(c -> c.getStatus() != null && c.getStatus().equals(dto.getStatus()));
		}

		if (dto.getStartDate() != null && !dto.getStartDate().isEmpty() && dto.getEndDate() != null
				&& !dto.getEndDate().isEmpty()) {

			LocalDate start = LocalDate.parse(dto.getStartDate());
			LocalDate end = LocalDate.parse(dto.getEndDate());

			stream = stream.filter(c -> {
				if (c.getStartDate() == null)
					return false;
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

		return HostPageResponseDto.<HostClassDto>builder().content(pageList).pageInfo(pageInfo).build();
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
		HostClassDto hostclass = hostClassRepository.findById(classId)
				.orElseThrow(() -> new Exception("해당 클래스가 존재하지 않습니다.")).toDto();
		return hostclass;
	}

	// 관리자 페이지 > 클래스 관리
	@Override
	public Page<AdminClassDto> getHostClassListForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception {
		log.info("클래스 목록 관리 조회 - cond : {}, pageable : {}", cond, pageable);
		try {
			Page<AdminClassDto> content = hostClassRepository.searchClassForAdmin(cond, pageable);

			Long total = hostClassRepository.countClasses(cond); // 검색 조건에 따른 페이지 계산
			return content;
		} catch (Exception e) {
			log.error("getClassList 에러 상새 : ", e);
			throw e;
		}
	}

	/*** 관리자가 강사 클래스를 승인 */
	@Override
	@Transactional
	public void approveClass(Integer classId) throws Exception {
		try {
			// 1. 클래스 상태 업데이트
			int updatedStatus = hostClassRepository.updateClassStatus(classId);
			if (updatedStatus == 0) {
				throw new RuntimeException("업데이트 할 상태가 없습니다.");
			}

			// 2. 클래스 정보 조회 및 userId 추출
			HostClass hostClass = hostClassRepository.findById(classId)
					.orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다."));

			Host host = hostClass.getHost();
			if (host == null) {
				throw new RuntimeException("호스트 정보를 찾을 수 없습니다.");
			}

			Integer userId = host.getUserId(); // 이미 userId를 올바르게 가져오고 있음
			if (userId == null) {
				throw new RuntimeException("사용자 ID를 찾을 수 없습니다.");
			}

			// 3. 알람 생성 및 발송
			AlarmDto alarmDto = AlarmDto.builder()
					.alarmType(2) // 클래스링 알람
					.title("클래스 승인 안내")
					.receiverId(userId) // userId 사용
					.senderId(1) // 관리자 ID
					.senderNickname("관리자")
					.content("클래스가 승인 되었습니다")
					.build();

			log.info("클래스 승인 알람 발송 - 클래스ID: {}, 수신자 userId: {}", classId, userId);
			alarmService.sendAlarm(alarmDto);

		} catch (Exception e) {
			log.error("클래스 승인 처리 중 오류 발생 - 클래스ID: {}, 오류: {}", classId, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void rejectClass(Integer classId) throws Exception {
		HostClass hostClass = hostClassRepository.findById(classId)
				.orElseThrow(() -> new RuntimeException("클래스르 찾을 수 없습니다."));
		ClassCalendar calendar = classCalendarRepository.findFirstByHostClassClassId(classId);
				if(calendar == null) {
					throw new RuntimeException("캘린더 정보를 찾을 수 없습니다.");
				}
				calendar.setStatus("거절");
				classCalendarRepository.save(calendar);
	}

	@Override
	public AdminClassDetailDto getClassDetailForAdmin(Integer classId) throws Exception {
		// 1. 클래스 기본 정보 조회
		HostClass hostClass = hostClassRepository.findById(classId)
				.orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다."));

		// 2. 캘린더 정보 조회 - 해당 클래스의 모든 일정 조회
		Set<Integer> classIds = Set.of(classId);
		List<ClassCalendar> calendars = classCalendarRepository.findByHostClassClassIdIn(classIds);

		ClassCalendar mainCalendar = calendars.isEmpty() ? null : calendars.get(0); // 기본 정보용

		// 3. 스케줄 목록 생성 (일정 id, 클래스일자, 상태, 등록수)
		List<AdminClassDetailDto.ScheduleDto> schedules = calendars.stream()
				.map(calendar -> AdminClassDetailDto.ScheduleDto.builder()
						.calendarId(calendar.getCalendarId())
						.startDate(calendar.getStartDate())
						.endDate(calendar.getEndDate())
						.status(calendar.getStatus())
						.registeredCount(calendar.getRegisteredCount())
						.build())
				.collect(Collectors.toList());

		// 디버깅용 로그 추가
		log.info("=== 클래스 상세 조회 시작 ===");
		log.info("classId: {}, calendars size: {}, schedules size: {}",
				classId, calendars.size(), schedules.size());

		// 4. 모든 캘린더의 수강생 조회 (캘린더별로 다른 수강생이 있을 수 있음)
		List<AdminClassDetailDto.StudentDto> allStudents = new ArrayList<>();

		log.info("=== 수강생 조회 시작 ===");
		log.info("전체 캘린더 수: {}", calendars.size());

		for (ClassCalendar calendar : calendars) {
			log.info("캘린더 ID: {}, 상태: {}, 등록수: {}",
					calendar.getCalendarId(), calendar.getStatus(), calendar.getRegisteredCount());

			List<ClassRegist> registList = classRegistRepository.findByClassCalendarCalendarId(calendar.getCalendarId());
			log.info("캘린더 ID {} 의 실제 등록자 수: {}", calendar.getCalendarId(), registList.size());

			// 해당 캘린더의 수강생들을 StudentDto로 변환
			List<AdminClassDetailDto.StudentDto> calendarStudents = registList.stream()
					.map(regist -> {
						User user = userRepository.findById(regist.getStudentId()).orElse(null);
						log.info("등록자 정보: studentId={}, user={}, calendarId={}",
								regist.getStudentId(),
								user != null ? user.getName() : "null",
								calendar.getCalendarId());

						return AdminClassDetailDto.StudentDto.builder()
								.userId(user != null && user.getUserId() != null ? String.valueOf(user.getUserId()) : "")
								.name(user != null ? user.getName() : "")
								.phone(user != null ? user.getTel() : "")
								.email(user != null ? user.getEmail() : "")
								.regDate(user != null ? user.getRegDate() : null)
								.status(calendar.getStatus()) // 해당 캘린더의 상태
								.build();
					})
					.filter(student -> !student.getUserId().isEmpty()) // 유효한 사용자만 필터링
					.collect(Collectors.toList());

			log.info("캘린더 ID {} 에서 변환된 수강생 수: {}", calendar.getCalendarId(), calendarStudents.size());
			allStudents.addAll(calendarStudents);
		}

		log.info("중복 제거 전 전체 수강생 수: {}", allStudents.size());

		// 5. 중복 제거 (같은 사용자가 여러 일정에 등록된 경우, 첫 번째 등록 정보 유지)
		List<AdminClassDetailDto.StudentDto> uniqueStudents = allStudents.stream()
				.collect(Collectors.toMap(
						student -> student.getUserId(),
						student -> student,
						(existing, replacement) -> existing // 첫 번째 등록 정보 유지
				))
				.values()
				.stream()
				.collect(Collectors.toList());

		log.info("중복 제거 후 최종 수강생 수: {}", uniqueStudents.size());

		// 6. DTO 생성 및 반환
		AdminClassDetailDto result = AdminClassDetailDto.builder()
				.classId(hostClass.getClassId())
				.className(hostClass.getName())
				.hostName(hostClass.getHost() != null ? hostClass.getHost().getName() : "")
				.processStatus(mainCalendar != null ? mainCalendar.getStatus() : "")
				.currentCount(mainCalendar != null ? mainCalendar.getRegisteredCount() : 0)
				.recruitMax(hostClass.getRecruitMax())
				.recruitMin(hostClass.getRecruitMin())
				.firstCategory(hostClass.getSubCategory() != null && hostClass.getSubCategory().getFirstCategory() != null ?
						hostClass.getSubCategory().getFirstCategory().getCategoryName() : "")
				.secondCategory(hostClass.getSubCategory() != null ? hostClass.getSubCategory().getSubCategoryName() : "")
				.price(hostClass.getPrice())
				.startDate(mainCalendar != null ? mainCalendar.getStartDate() : null)
				.endDate(mainCalendar != null ? mainCalendar.getEndDate() : null)
				.scheduleStart(hostClass.getScheduleStart())
				.scheduleEnd(hostClass.getScheduleEnd())
				.location(hostClass.getAddr())
				.detailAddr(hostClass.getDetailAddr())
				.description(hostClass.getDetailDescription())
				.keywords(hostClass.getKeywords())
				.inclusion(hostClass.getIncluision())
				.preparation(hostClass.getPreparation())
				.caution(hostClass.getCaution())
				.portfolioName(hostClass.getPortfolio())
				.materialName(hostClass.getMaterial())
				.imgName1(hostClass.getImg1())
				.imgName2(hostClass.getImg2())
				.imgName3(hostClass.getImg3())
				.imgName4(hostClass.getImg4())
				.imgName5(hostClass.getImg5())
				.calendarId(mainCalendar != null ? mainCalendar.getCalendarId() : null)
				.schedules(schedules)  // 스케줄 목록 추가
				.students(uniqueStudents) // 모든 캘린더의 수강생 목록
				.build();

		log.info("=== 클래스 상세 조회 완료 ===");
		log.info("반환할 수강생 수: {}", uniqueStudents.size());

		return result;
	}

	@Override
	public Integer updateClass(HostClassDto hostClassDto) throws Exception {
		HostClassDto classDto = hostClassRepository.findByClassId(hostClassDto.getClassId()).toDto();
		MultipartFile[] files = { hostClassDto.getImg1(), hostClassDto.getImg2(), hostClassDto.getImg3(),
				hostClassDto.getImg4(), hostClassDto.getImg5(), hostClassDto.getMaterial(),
				hostClassDto.getPortfolio() };
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				File upFile = new File(iuploadPath, file.getOriginalFilename());
				file.transferTo(upFile);
			}
		}

		if (files[0] != null && !files[0].isEmpty()) {
			hostClassDto.setImgName1(files[0].getOriginalFilename());
		} else {
			hostClassDto.setImgName1(classDto.getImgName1());
		}
		if (files[1] != null && !files[1].isEmpty()) {
			hostClassDto.setImgName2(files[1].getOriginalFilename());
		} else {
			hostClassDto.setImgName2(classDto.getImgName2());
		}

		if (files[2] != null && !files[2].isEmpty()) {
			hostClassDto.setImgName3(files[2].getOriginalFilename());
		} else {
			hostClassDto.setImgName3(classDto.getImgName3());
		}
		if (files[3] != null && !files[3].isEmpty()) {
			hostClassDto.setImgName4(files[3].getOriginalFilename());
		} else {
			hostClassDto.setImgName4(classDto.getImgName4());
		}
		if (files[4] != null && !files[4].isEmpty()) {
			hostClassDto.setImgName5(files[4].getOriginalFilename());
		} else {
			hostClassDto.setImgName5(classDto.getImgName5());
		}
		if (files[5] != null && !files[5].isEmpty()) {
			hostClassDto.setMaterialName(files[5].getOriginalFilename());
		} else {
			hostClassDto.setMaterialName(classDto.getMaterialName());
		}

		hostClassRepository.save(hostClassDto.toEntity());

		return hostClassDto.getClassId();

	}

	@Override
	public List<UserDto> selectClassStudentList(Integer calendarId) throws Exception {
		List<ClassRegist> classRegistList = classRegistRepository.findByClassCalendarCalendarId(calendarId);
		List<User> studnetList = new ArrayList<>();
		for (ClassRegist regist : classRegistList) {
			studnetList.add(userRepository.findById(regist.getUser().getUserId()).get());
		}
		List<UserDto> dtoList = studnetList.stream().map(User::toDto).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	public List<UserDto> selectStudentList(Integer hostId) throws Exception {
		List<ClassRegist> regList = classRegistRepository.findByCalendar_HostClass_Host_HostId(hostId);
		List<UserDto> userDtoList = new ArrayList<>();
		for (ClassRegist reg : regList) {
			User user = userRepository.findById(reg.getUser().getUserId()).get();
			userDtoList.add(user.toDto());
		}

		return userDtoList;
	}

	@Override
	public Page<UserDto> searchStudents(StudentSearchRequestDto dto) throws Exception {
		PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize());
		Page<User> resultPage = hostClassRepository.searchClassStudent(dto, pageable);
		return resultPage.map(User::toDto);
	}

	@Override
	public List<CalendarUserDto> searchStudentClass(Integer hostId, Integer userId) throws Exception {
		List<CalendarUserDto> dtoList = classRegistRepository.findByStudentClass(hostId, userId);
		return dtoList;
	}

	@Override
	public List<HostClassDto> getRecommendClassesInDetail(Integer subCategoryId, Integer classId) throws Exception {
		SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new Exception("해당 카테고리가 존재하지 않습니다.ㄴ"));
		List<HostClassDto> result = hostClassRepository.findRecommendClassesInDetail(subCategoryId,
				subCategory.getFirstCategory().getCategoryId(), classId);
		return result;
	}

	@Override
	public Map<String, Object> hostRateCount(Integer hostId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 문의 응답률
		List<Inquiry> inquiryList = inquiryRepository.hostInquiryCount(hostId);
		int size = inquiryList.size();
		int count = 0;
		for (Inquiry iq : inquiryList) {
			if (iq.getState() == 1) {
				count++;
			}
		}
		if (size == 0) {
			map.put("inquiryRate", 0.0);
		} else {
			Double result = (count / (double) size) * 100;
			result = Math.round(result * 100.0) / 100.0;
			map.put("inquiryRate", result);

		}

		// 리뷰 총 갯수
		List<Review> reviewList = reviewRepository.findByHostHostId(hostId);
		map.put("reviewRate", reviewList.size());

		// 평점
		int star = 0;
		for (Review review : reviewList) {
			star += review.getStar();
		}

		double starRate = ((double) star / reviewList.size()) * 100;
		starRate = Math.round(starRate * 100.0) / 100.0;
		map.put("starRate", starRate);

		// 이번달 진행한 클래스갯수
		List<ClassCalendar> calendarList = hostClassRepository.findByHostId(hostId);
		int calendarCount = 0;
		for (ClassCalendar cal : calendarList) {
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfThisMonth = currentDate.withDayOfMonth(1);
			LocalDate lastDayOfThisMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
			if (cal.getStatus().equals("종료") && !cal.getStartDate().before(Date.valueOf(firstDayOfThisMonth))
					&& !cal.getStartDate().after(Date.valueOf(lastDayOfThisMonth))) {
				calendarCount++;
			}
		}
		map.put("calendarCount", calendarCount);

		// 이번달 취소 건수
		int cancleCount = 0;
		for (ClassCalendar cal : calendarList) {
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfThisMonth = currentDate.withDayOfMonth(1);
			LocalDate lastDayOfThisMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
			if (cal.getStatus().equals("폐강")
					|| cal.getStatus().equals("반려") && !cal.getStartDate().before(Date.valueOf(firstDayOfThisMonth))
					&& !cal.getStartDate().after(Date.valueOf(lastDayOfThisMonth))) {
				cancleCount++;
			}
		}
		map.put("cancleCount", cancleCount);

		// 전체정상금액
		List<AdminSettlement> settlementList = settlementRepository.findByHostIdsettlementList(hostId);
		int settleCount = 0;
		int allSettleCount = 0;
		for (AdminSettlement settle : settlementList) {
			if(settle.getSettlementStatus().equals("CP")) {
				settleCount += settle.getSettlementAmount();
				allSettleCount++;
			}
		}
		map.put("settleCount", settleCount);

		//전체 결제 건수
		map.put("payCount", allSettleCount);

		//이번달 판매금액
		int thisMonthSettleCount = 0;
		for(AdminSettlement settle : settlementList) {
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfThisMonth = currentDate.withDayOfMonth(1);
			LocalDate lastDayOfThisMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
			if(settle.getSettlementStatus().equals("CP") && !settle.getSettlementDate().isBefore(firstDayOfThisMonth) && !settle.getSettlementDate().isAfter(lastDayOfThisMonth)) {
				thisMonthSettleCount += settle.getSettlementAmount();
			}
		}
		map.put("thisMonthSettle",thisMonthSettleCount);

		return map;
	}

}
