package com.dev.moyering.host.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.classring.dto.InquiryResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.dto.InquirySearchRequestDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.host.repository.InquiryRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final InquiryRepository inquiryRepository;
	private final UserRepository userRepository;
	private final ClassCalendarRepository calendarRepository;
	private final HostClassRepository hostClassRepository;
	private final AlarmService alarmService;
	private final HostRepository hostRepository;
	
	@Override
	public PageResponseDto<InquiryDto> getInquiryListByClassId(Integer classId, int page, int size) throws Exception {
		Pageable pageable = PageRequest.of(page, size);
		Page<Inquiry> inquiryPage = inquiryRepository.findInquiriesByClassId(classId,pageable);
		
		List<InquiryDto> dtoList = inquiryPage.getContent().stream()
				.map(Inquiry::toDto)
				.collect(Collectors.toList());
		
		return PageResponseDto.<InquiryDto>builder()
				.content(dtoList)
				.currentPage(inquiryPage.getNumber()+1)
				.totalPages(inquiryPage.getTotalPages())
				.totalElements(inquiryPage.getTotalElements())
				.build();
	}
	
	@Transactional
	@Override
	public Integer writeInquriy(InquiryDto dto) throws Exception {
		User user = userRepository.findById(dto.getUserId())
	            .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
	    ClassCalendar calendar = calendarRepository.findById(dto.getCalendarId())
	            .orElseThrow(() -> new Exception("캘린더를 찾을 수 없습니다."));
	    
	    Inquiry inquiry = Inquiry.builder()
	    		.classCalendar(calendar)
	    		.user(user)
	    		.content(dto.getContent())
	    		.inquiryDate(Date.valueOf(LocalDate.now()))
	    		.state(0)
	    		.build();
	    inquiryRepository.save(inquiry);
	    
	    AlarmDto alarm =  AlarmDto.builder()
	    		.alarmType(2)
	    		.title("클래스 문의 등록")
	    		.content(calendar.getHostClass().getName()+"에 대한 문의가 등록되었습니다.")
	    		.receiverId(calendar.getHostClass().getHost().getUserId())
	    		.senderId(dto.getUserId())
	    		.senderNickname(dto.getStudentName())
	    		.build();

	    alarmService.sendAlarm(alarm);
		return inquiry.getInquiryId();
	}
	@Override
	public List<InquiryDto> selectInquiryByHostId(Integer hostId) throws Exception {
		List<HostClass> hostClassList = hostClassRepository.findByHostHostId(hostId);
		Set<Integer> classIdList = hostClassList.stream()
				.map(HostClass::getClassId)
				.collect(Collectors.toSet());
		List<ClassCalendar> calendarList = calendarRepository.findByHostClassClassIdIn(classIdList);
		List<Integer> calendarIdList = new ArrayList<>();
		for(ClassCalendar cCalendar : calendarList) {
			calendarIdList.add(cCalendar.getCalendarId());
		}
		List<Inquiry> inquiryList = inquiryRepository.findByClassCalendarCalendarIdIn(calendarIdList);
		List<InquiryDto> inquiryDtoList = new ArrayList<>();
		for(Inquiry entity : inquiryList) {
			inquiryDtoList.add(entity.toDto());
		}
		for(InquiryDto dto : inquiryDtoList) {
			String studentName = userRepository.findById(dto.getUserId()).get().getNickName();
			dto.setStudentName(studentName);
		}
		return inquiryDtoList;
	}
	@Override
	public void replyInquiry(Integer inquiryId, Integer hostId, String iqResContent) throws Exception {
		Inquiry inquiry = inquiryRepository.findById(inquiryId).get();
		InquiryDto inquiryDto = inquiry.toDto();
		if(hostId != null && iqResContent != null) {
			inquiryDto.setHostId(hostId);
			inquiryDto.setIqResContent(iqResContent);
			inquiryDto.setResponseDate(new Date(System.currentTimeMillis()));
			inquiryDto.setState(1);
		}
		
		inquiryRepository.save(inquiryDto.toEntity());
		
		Host host = hostRepository.findById(hostId).get();
		
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(2)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("문의 답변 알림") // 필수 사항
				.receiverId(inquiry.getUser().getUserId())
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
	public Page<InquiryDto> searchInquiries(InquirySearchRequestDto dto) throws Exception {
		PageRequest pageable = PageRequest.of(dto.getPage(),dto.getSize());
		Page<Inquiry> resultPage = inquiryRepository.searchInquiries(dto, pageable);
		return resultPage.map(Inquiry::toDto);
	}
	@Override
	public PageResponseDto<InquiryResponseDto> getMyInquiries(UtilSearchDto dto) throws Exception {
		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
		Page<InquiryResponseDto> pageResult = inquiryRepository.findInquriesByUserId(dto, pageable);
		
		return PageResponseDto.<InquiryResponseDto>builder()
				.content(pageResult.getContent())
				.currentPage(pageResult.getNumber()+1)
				.totalPages(pageResult.getTotalPages())
				.totalElements(pageResult.getTotalElements())
				.build();
	}
	

}
