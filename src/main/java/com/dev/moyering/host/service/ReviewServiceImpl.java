package com.dev.moyering.host.service;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.admin.service.AdminBadgeScoreService;
import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.host.repository.ReviewRepository;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.user.service.UserBadgeService;
import com.dev.moyering.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final HostClassRepository classRepository;
	private final ClassCalendarRepository calendarRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final AdminBadgeScoreService adminBadgeScoreService;
	private final UserBadgeService userBadgeService;
	private final AlarmService alarmService;
	private final HostRepository hostRepository;
    @Value("${iupload.path}")
    private String uploadPath;

	
	@Override
	public List<ReviewDto> getReviewByHostId(Integer hostId) {
		return reviewRepository.findTop3ByHost_HostIdOrderByReviewDateDesc(hostId)
				.stream().map(r->r.toDto()).collect(Collectors.toList());
	}
	
	@Override
	public PageResponseDto<ReviewDto> getAllReviewByHostId(Integer hostId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Review> reviewPage = reviewRepository.findReviewsByHostId(hostId, pageable);
		
		//dto로 변환
		List<ReviewDto> dtoList = reviewPage.getContent().stream()
				.map(Review::toDto)
				.collect(Collectors.toList());
		//PageResponseDto로 변환
		return PageResponseDto.<ReviewDto>builder()
				.content(dtoList)
				.currentPage(reviewPage.getNumber()+1)
				.totalPages(reviewPage.getTotalPages())
				.totalElements(reviewPage.getTotalElements())
				.build();
	}

	@Override
	public PageResponseDto<ReviewDto> getReviewsForHost(Integer hostId, int page, int size) throws Exception {
		Pageable pageable = PageRequest.of(page, size);
		Page<Review> reviewPage = reviewRepository.findReviewsByHostId(hostId, pageable);
		
		List<ReviewDto> dtoList = reviewPage.getContent().stream()
				.map(Review::toDto)
				.collect(Collectors.toList());
		
		return PageResponseDto.<ReviewDto>builder()
				.content(dtoList)
				.currentPage(reviewPage.getNumber()+1)
				.totalPages(reviewPage.getTotalPages())
				.totalElements(reviewPage.getTotalElements())
				.build();
	}

	@Override
	public Page<ReviewDto> searchReviews(ReviewSearchRequestDto dto) throws Exception {
		PageRequest pageable = PageRequest.of(dto.getPage(),dto.getSize());
		Page<Review> resultPage = reviewRepository.getReviewsForHost(dto, pageable);
		return resultPage.map(Review::toDto);
	}

	@Override
	public List<ReviewDto> getReviews(Integer hostId) throws Exception {
			List<HostClass> hostClassList = classRepository.findByHostHostId(hostId);
			Set<Integer> classIdList = hostClassList.stream()
					.map(HostClass::getClassId)
					.collect(Collectors.toSet());
			List<ClassCalendar> calendarList = calendarRepository.findByHostClassClassIdIn(classIdList);
			List<Integer> calendarIdList = new ArrayList<>();
			for(ClassCalendar cCalendar : calendarList) {
				calendarIdList.add(cCalendar.getCalendarId());
			}
			List<Review> reviewList = reviewRepository.findByClassCalendarCalendarIdIn(calendarIdList);
			List<ReviewDto> reviewDtoList = new ArrayList<>();
			for(Review entity : reviewList) {
				reviewDtoList.add(entity.toDto());
			}
			for(ReviewDto dto : reviewDtoList) {
				String studentName = userRepository.findById(dto.getUserId()).get().getNickName();
				dto.setStudentName(studentName);
			}
			return reviewDtoList;
		
	}

	@Override
	public void replyReview(Integer reviewId, Integer hostId, String revRegContent) throws Exception {
		Review review = reviewRepository.findById(reviewId).get();
		ReviewDto reviewDto = review.toDto();
		if(hostId != null && revRegContent!=null) {
			reviewDto.setHostId(hostId);
			reviewDto.setRevRegCotnent(revRegContent);
			reviewDto.setResponseDate(new Date(System.currentTimeMillis()));
			reviewDto.setState(1);
		}
		reviewRepository.save(reviewDto.toEntity());
		
		Host host = hostRepository.findById(hostId).get();
		
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(2)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("문의 답변 알림") // 필수 사항
				.receiverId(review.getUser().getUserId())
				//수신자 유저 아이디
				.senderId(hostId)
				//발신자 유저 아이디 
				.senderNickname(host.getName())
				//발신자 닉네임 => 시스템/관리자가 발송하는 알람이면 메니저 혹은 관리자, 강사가 발송하는 알람이면 강사테이블의 닉네임, 그 외에는 유저 테이블의 닉네임(마이페이지 알림 내역에서 보낸 사람으로 보여질 이름)
				.content(host.getName()+"강사님께서 답변을 완료하였습니다.")//알림 내용
				.build();
		alarmService.sendAlarm(alarmDto);
		
	}
	@Override
	public PageResponseDto<WritableReviewResponseDto> getWritableReviews(UtilSearchDto dto) throws Exception {
	    Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
	    Page<WritableReviewResponseDto> pageResult = reviewRepository.findWritableReviews(dto, pageable);

	    return PageResponseDto.<WritableReviewResponseDto>builder()
	            .content(pageResult.getContent())
	            .currentPage(pageResult.getNumber() + 1)
	            .totalPages(pageResult.getTotalPages())
	            .totalElements(pageResult.getTotalElements())
	            .build();
	}
	@Override
	public PageResponseDto<UserReviewResponseDto> getDoneReviews(UtilSearchDto dto) throws Exception {
	    Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
	    Page<UserReviewResponseDto> pageResult = reviewRepository.findDoneReviews(dto, pageable);

	    return PageResponseDto.<UserReviewResponseDto>builder()
	            .content(pageResult.getContent())
	            .currentPage(pageResult.getNumber() + 1)
	            .totalPages(pageResult.getTotalPages())
	            .totalElements(pageResult.getTotalElements())
	            .build();
	}
	
	@Transactional
	@Override
	public Integer writeReview(ReviewDto reviewDto) throws Exception {
		//이미지가 있을 때
		String savedFileName = null;
		MultipartFile file = reviewDto.getReviewImg();
		
		if (file != null && !file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			savedFileName = uuid + "_" + originalFilename;
			
			File saveFile = new File(uploadPath,savedFileName);
			file.transferTo(saveFile);
			reviewDto.setReviewImgName(savedFileName);		
		}
		reviewDto.setReviewDate(new Date(System.currentTimeMillis()));
		reviewDto.setState(0);
		
		Review entity = reviewDto.toEntity();
        Review saved = reviewRepository.save(entity);
        
        //리뷰 작성 시 포인트 획득
        //증가시킬 포인트 찾기
        Integer score = adminBadgeScoreService.getScoreByTitle("클래스 후기 작성");
        //유저의 활동점수 증가
        userService.addScore(reviewDto.getUserId(), score);
        //뱃지 획득 가능 여부 확인
        userBadgeService.giveBadgeWithScore(reviewDto.getUserId());
        
        System.out.println(saved);
	    ClassCalendar calendar = calendarRepository.findById(reviewDto.getCalendarId())
	            .orElseThrow(() -> new Exception("캘린더를 찾을 수 없습니다."));
	    
	    AlarmDto alarm =  AlarmDto.builder()
	    		.alarmType(2)
	    		.title("클래스 리뷰 등록")
	    		.content(calendar.getHostClass().getName() +"에 대한 리뷰가 등록되었습니다.")
	    		.receiverId(calendar.getHostClass().getHost().getUserId())
	    		.senderId(reviewDto.getUserId())
	    		.senderNickname(reviewDto.getStudentName())
	    		.build();
	    
        alarmService.sendAlarm(alarm);
        return saved.getReviewId();
    }


}
