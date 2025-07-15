package com.dev.moyering.host.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.classring.service.ClassPaymentService;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.ScheduleDetailRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassCalendarServiceImpl implements ClassCalendarService {
	private final ClassCalendarRepository classCalendarRepository;
	private final UserRepository userRepository;
	private final ScheduleDetailRepository scheduleDetailRepository;
	private final ClassPaymentService classPaymentService;
	private final AlarmService alarmService;
	private final ClassRegistService classRegistService;

	@Override
	public List<HostClassDto> getHotHostClasses() throws Exception {
		List<ClassCalendar> classList = classCalendarRepository.findTop4ByDistinctHostClass();

		return classList.stream()
				.map(cal -> {
					HostClassDto dto = cal.getHostClass().toDto();
					dto.setStartDate(cal.getStartDate());
					return dto;
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<HostClassDto> getRecommendHostClassesForUser2(Integer userId) throws Exception {
		List<ClassCalendar> classes;
		if (userId == null) {
			classes = classCalendarRepository.findRecommendClassesForUser2(null);
		} else {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
			classes = classCalendarRepository.findRecommendClassesForUser2(user);
		}
		List<HostClassDto> results;
		return classes.stream()
				.map(cc -> {
					HostClassDto dto = cc.getHostClass().toDto();
					dto.setStartDate(cc.getStartDate());
					return dto;
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<ClassCalendarDto> selectCalednarByClassId(Integer classId) throws Exception {
		List<ClassCalendar> calendarList = classCalendarRepository.findByHostClassClassId(classId);
		List<ClassCalendarDto> calDtoList = new ArrayList<>();
		for(ClassCalendar cal : calendarList) {
			calDtoList.add(cal.toDto());
		}
		return calDtoList;
	}
	public List<ClassCalendarDto> getClassCalendarByHostClassId(Integer classId) throws Exception {
		List<ClassCalendar> classes = classCalendarRepository.findAllByHostClass_ClassIdAndStatus(classId, "모집중");
		return classes.stream()
				.map(cc -> cc.toDto()).collect(Collectors.toList());
	}

	@Override
	public List<ClassCalendarDto> getMyClassSchedule(Integer userId) throws Exception {
		List<ClassCalendarDto> classList = classCalendarRepository.findAllScheduleWithDetailsByUserId(userId);
		
		for (ClassCalendarDto dto : classList ) {
			scheduleDetailRepository.findFirstByHostClass_ClassIdOrderByStartTimeAsc(dto.getClassId())
				.ifPresent(d-> {
					dto.setStartTime(d.getStartTime());
				});
		}
		return classList;
	}

	@Transactional
	@Override
	public void checkHostClassStatus() throws Exception {
		System.out.println("-------------------------------");
        LocalDate localDate = LocalDate.now().plusDays(2);
        Date classDate = java.sql.Date.valueOf(localDate);
        List<ClassCalendar> list = classCalendarRepository.findByStartDateLessThanEqualAndStatus(classDate, "모집중");

        for (ClassCalendar cal : list) {
            int registered = cal.getRegisteredCount();
            int min = cal.getHostClass().getRecruitMin();

            if (registered >= min) {
                cal.changeStatus("모집마감");
                
              //개강하는 알림 (등록된 모든 사람들에게)
                List<ClassRegist> registerdUsers =  classRegistService.getregisterdUsersByClassId(cal.getCalendarId());
                
                for (ClassRegist cr :registerdUsers ) {
        			User user = userRepository.findById(cr.getUser().getUserId())
        					.orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + cr));
        			
	    			AlarmDto alarm =  AlarmDto.builder()
	    		    		.alarmType(2)
	    		    		.title("클래스 개강 안내")
	    		    		.content(cal.getHostClass().getName()+" 개강이 확정되었습니다. 수업 시간을 잊지마세요.")
	    		    		.receiverId(cal.getHostClass().getHost().getUserId())
	    		    		.senderId(user.getUserId())
	    		    		.senderNickname(user.getNickName())
	    		    		.build();
	    	        alarmService.sendAlarm(alarm);
                }
            } else {
                cal.changeStatus("폐강");
                classPaymentService.refundAllForCalendar(cal.getCalendarId());
                
                //폐강하는 알림 (등록된 모든 사람들에게)
                List<ClassRegist> registerdUsers =  classRegistService.getregisterdUsersByClassId(cal.getCalendarId());
                
                for (ClassRegist cr :registerdUsers ) {
        			User user = userRepository.findById(cr.getUser().getUserId())
        					.orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + cr));
        			
	    			AlarmDto alarm =  AlarmDto.builder()
	    		    		.alarmType(2)
	    		    		.title("클래스 폐강 안내")
	    		    		.content(cal.getHostClass().getName()+" 인원 미달로 인해 폐강되었습니다.")
	    		    		.receiverId(cal.getHostClass().getHost().getUserId())
	    		    		.senderId(user.getUserId())
	    		    		.senderNickname(user.getNickName())
	    		    		.build();
	    	        alarmService.sendAlarm(alarm);
                }
                
            }
        }
        classCalendarRepository.saveAll(list);
        
	}

	@Transactional
	@Override
	public void changeStatusToFinished() throws Exception {
        LocalDate localDate = LocalDate.now().minusDays(1);
        Date classDate = java.sql.Date.valueOf(localDate);
        List<ClassCalendar> list = classCalendarRepository.findByStartDateLessThanEqualAndStatus(classDate, "모집마감");
        
        for (ClassCalendar cal : list) {
        	cal.changeStatus("종료");
        }
        
        classCalendarRepository.saveAll(list);
	}

}
