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
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.ReviewRepository;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final HostClassRepository classRepository;
	private final ClassCalendarRepository calendarRepository;
	private final UserRepository userRepository;
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
        return saved.getReviewId();
    }


}
