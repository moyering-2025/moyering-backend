package com.dev.moyering.host.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.entity.AdminNotice;
import com.dev.moyering.admin.service.AdminCouponService;
import com.dev.moyering.admin.service.AdminNoticeService;
import com.dev.moyering.admin.service.AdminSettlementService;
import com.dev.moyering.host.dto.CalendarUserDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.dto.InquirySearchRequestDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;
import com.dev.moyering.host.dto.StudentSearchRequestDto;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.ClassRegistRepository;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.InquiryService;
import com.dev.moyering.host.service.ReviewService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.service.UserService;
import com.dev.moyering.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HostClassController {
	private final HostClassService hostClassService;
	private final InquiryService inquiryService;
	private final AdminCouponService adminCouponService;
	private final ClassRegistRepository classRegistService;
	private final ClassCalendarRepository calendarRepository;
	private final UserService userService;
	private final ReviewService reviewService;
	private final AdminNoticeService adminNoticeService;
	private final AdminSettlementService settlementService;

	@GetMapping("/host/calendar")
	public ResponseEntity<List<HostClassDto>> selectClassCalendar(@RequestParam Integer hostId) {
		try {
			List<HostClassDto> hostClasses = hostClassService.selectHostClassByHostId(hostId);
			return new ResponseEntity<>(hostClasses, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/inquiry")
	public ResponseEntity<List<InquiryDto>> selectInquiry(@RequestParam Integer hostId) {
		try {
			List<InquiryDto> inquiryDtoList = inquiryService.selectInquiryByHostId(hostId);
			return ResponseEntity.ok(inquiryDtoList);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/inquiry/search")
	public ResponseEntity<HostPageResponseDto<InquiryDto>> searchInquries(@RequestBody InquirySearchRequestDto dto) {
		System.out.println(dto.getReplyStatus());
		try {
			Page<InquiryDto> page = inquiryService.searchInquiries(dto);

			// PageInfo 생성
			PageInfo pageInfo = new PageInfo();
			int curPage = page.getNumber() + 1;
			int allPage = page.getTotalPages();
			int blockSize = 5;
			int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
			int endPage = Math.min(startPage + blockSize - 1, allPage);

			pageInfo.setCurPage(curPage);
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			HostPageResponseDto<InquiryDto> response = HostPageResponseDto.<InquiryDto>builder()
					.content(page.getContent()).pageInfo(pageInfo).build();

			for (InquiryDto inquiryDto : response.getContent()) {
				UserDto user = userService.findUserByUserId(inquiryDto.getUserId());
				inquiryDto.setStudentName(user.getNickName());
			}
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/review")
	public ResponseEntity<List<ReviewDto>> selectReview(@RequestParam Integer hostId) {
		try {
			List<ReviewDto> reviewList = reviewService.getReviews(hostId);
			return new ResponseEntity<>(reviewList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/review/search")
	public ResponseEntity<HostPageResponseDto<ReviewDto>> studentReview(@RequestBody ReviewSearchRequestDto dto) {
		try {
			Page<ReviewDto> page = reviewService.searchReviews(dto);
			System.out.println(dto.getCalendarId());

			PageInfo pageInfo = new PageInfo();
			int curPage = page.getNumber() + 1;
			int allPage = page.getTotalPages();
			int blockSize = 5;
			int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
			int endPage = Math.min(startPage + blockSize - 1, allPage);

			pageInfo.setCurPage(curPage);
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			HostPageResponseDto<ReviewDto> response = HostPageResponseDto.<ReviewDto>builder()
					.content(page.getContent()).pageInfo(pageInfo).build();

			for (ReviewDto reviewDto : response.getContent()) {
				UserDto user = userService.findUserByUserId(reviewDto.getUserId());
				reviewDto.setStudentName(user.getNickName());
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/reviewReply")
	public ResponseEntity<Object> reviewReply(@RequestParam Integer reviewId, @RequestParam Integer hostId,
			@RequestParam String revRegContent) {
		try {
			reviewService.replyReview(reviewId, hostId, revRegContent);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/inquiryReply")
	public ResponseEntity<Object> inquiryReply(@RequestParam Integer inquiryId, @RequestParam Integer hostId,
			@RequestParam String iqResContent) {
		try {
			System.out.println("iq:"+iqResContent);
			inquiryService.replyInquiry(inquiryId, hostId, iqResContent);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/couponList")
	public ResponseEntity<List<AdminCouponDto>> hostCouponList() {
		try {
			List<AdminCouponDto> couponList = adminCouponService.selectHostAllCoupon("HT");
			return new ResponseEntity<>(couponList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/classStudentList")
	public ResponseEntity<List<UserDto>> classStudentList(@RequestParam Integer calendarId) {
		try {
			List<UserDto> studnetList = hostClassService.selectClassStudentList(calendarId);
			return new ResponseEntity<>(studnetList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/studentList")
	public ResponseEntity<List<UserDto>> studentList(@RequestParam Integer hostId) {
		try {
			List<UserDto> studentList = hostClassService.selectStudentList(hostId);
			return new ResponseEntity<>(studentList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/notice")
	public ResponseEntity<List<AdminNotice>> hostNotice() {
		try {
			List<AdminNotice> list = adminNoticeService.selectAllNotice();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/student/search")
	public ResponseEntity<HostPageResponseDto<UserDto>> studentSearch(@RequestBody StudentSearchRequestDto dto) {
		try {
			Page<UserDto> page = hostClassService.searchStudents(dto);

			PageInfo pageInfo = new PageInfo();
			int curPage = page.getNumber() + 1;
			int allPage = page.getTotalPages();
			int blockSize = 5;
			int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
			int endPage = Math.min(startPage + blockSize - 1, allPage);

			pageInfo.setCurPage(curPage);
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			HostPageResponseDto<UserDto> response = HostPageResponseDto.<UserDto>builder().content(page.getContent())
					.pageInfo(pageInfo).build();

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/student/classes")
	public ResponseEntity<List<CalendarUserDto>> studentClass(@RequestParam Integer hostId,
			@RequestParam Integer userId) {
		try {
			List<CalendarUserDto> list = hostClassService.searchStudentClass(hostId, userId);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/host/settlementList")
	public ResponseEntity<HostPageResponseDto<AdminSettlementDto>> settlementList(@RequestBody SettlementSearchRequestDto dto){
		try {
			Page<AdminSettlementDto> page = settlementService.getHostSettlementList(dto);
			
			PageInfo pageInfo = new PageInfo();
			int curPage = page.getNumber() + 1;
			int allPage = page.getTotalPages();
			int blockSize = 5;
			int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
			int endPage = Math.min(startPage + blockSize - 1, allPage);

			pageInfo.setCurPage(curPage);
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);
			
			HostPageResponseDto<AdminSettlementDto> response = HostPageResponseDto.<AdminSettlementDto>builder().content(page.getContent())
					.pageInfo(pageInfo).build();
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
	}
	
	@GetMapping("/host/hostRateCount")
	public ResponseEntity<Map<String,Object>> hostRateCount(@RequestParam Integer hostId){
		try {
			System.out.println(hostId);
			Map<String,Object> map = hostClassService.hostRateCount(hostId);
			System.out.print(map.get("inquiryRate"));
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}

}
