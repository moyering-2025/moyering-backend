package com.dev.moyering.scheuler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dev.moyering.host.service.ClassCalendarService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClassStatusScheduler {
    private final ClassCalendarService classCalendarService;
	
	// 매일 00:00에 모집 마감 또는 폐강 처리 (수업일 -2일 )
    @Scheduled(cron = "0 59 17 * * *") // 매일 12:02 PM 실행
	public void handleRecruitmentClosureOrCancellation() {
		try {
			System.out.println("==============================");
			classCalendarService.checkHostClassStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//매일 00:00에 수업 종료 처리 (수업일 + 1일, 모집마감이 수업들만)
	@Scheduled(cron="0 0 0 * * *")
    public void handleClassCompletion() {
		try {
			classCalendarService.changeStatusToFinished();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
