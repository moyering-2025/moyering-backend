package com.dev.moyering.admin.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.entity.VisitorLogs;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.admin.repository.AdminVisitorLogsRepository;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashBaordServiceImpl implements AdminDashBoardService {
	private final UserRepository userRepository;
	private final AdminVisitorLogsRepository visitorRepository;
	private final AdminSettlementRepository settlementRepository;
	private final ClassCalendarRepository calendarRepository;

	@Override
	public Map<String, Object> AdminDashBoard() throws Exception {
		Map<String, Object> map = new HashMap<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate firstDayOfThisMonth = currentDate.withDayOfMonth(1);
		LocalDate lastDayOfThisMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
		// 총 회원 수
		List<User> userList = userRepository.findAll();
		int thisMonthMemberCount = 0;
		for (User user : userList) {
			if (!user.getRegDate().before(Date.valueOf(firstDayOfThisMonth))
					&& !user.getRegDate().after(Date.valueOf(lastDayOfThisMonth))) {
				thisMonthMemberCount++;
			}
		}
		map.put("userList", userList.size());
		// 이번달 등록한 회원 수
		map.put("thisMonthMemberCount", thisMonthMemberCount);

		// 총 방문자 수
		List<VisitorLogs> visitorLogList = visitorRepository.findAll();
		int visitorLogCount = visitorLogList.size();
		map.put("visitorLogCount", visitorLogCount);
		// 오늘 방문자 수
		int currentDateVisitCount = 0;
		for (VisitorLogs log : visitorLogList) {
			if (!log.getVisitDate().isBefore(firstDayOfThisMonth) && !log.getVisitDate().isAfter(lastDayOfThisMonth)) {
				currentDateVisitCount++;
			}
		}
		map.put("currentDateVisitCount", currentDateVisitCount);
		// 월별 방문자수
		Map<Integer, Long> visitsByMonthRaw = visitorLogList.stream()
				.collect(Collectors.groupingBy(v -> v.getVisitDate().getMonthValue(), Collectors.counting()));
		// 누락된 월을 0으로 채운 Map (순서도 보장)
		Map<Integer, Long> visitsByMonth = new LinkedHashMap<>();
		for (int i = 1; i <= 12; i++) {
			// 존재하면 그대로, 없으면 0
			visitsByMonth.put(i, visitsByMonthRaw.getOrDefault(i, 0L));
		}
		map.put("visitsByMonth", visitsByMonth);

		// 분기별 방문자수
		Map<Integer, Long> visitsByQuarterRaw = visitorLogList.stream()
				.collect(Collectors.groupingBy(v -> (v.getVisitDate().getMonthValue() - 1) / 3 + 1, // 분기 계산
						Collectors.counting()));

		// 1~4분기까지 모두 포함하는 Map 생성 (순서 유지)
		Map<Integer, Long> visitsByQuarter = new LinkedHashMap<>();
		for (int i = 1; i <= 4; i++) {
			visitsByQuarter.put(i, visitsByQuarterRaw.getOrDefault(i, 0L));
		}
		map.put("visitsByQuarter", visitsByQuarter);

		// 년도별 방문자수
		Map<Integer, Long> visitsByYearRaw = visitorLogList.stream()
				.collect(Collectors.groupingBy(v -> v.getVisitDate().getYear(), Collectors.counting()));
		Map<Integer, Long> visitsByYear = new LinkedHashMap<>();
		for (int i = 2024; i <= 2028; i++) {
			visitsByYear.put(i, visitsByYearRaw.getOrDefault(i, 0L));
		}
		map.put("visitsByYear", visitsByYear);

		List<AdminSettlement> settlementList = settlementRepository.findAll();
		int allSettle = 0;
		int thisMonthSettle = 0;
		int todaySettle = 0;
		for (AdminSettlement settle : settlementList) {
			if (settle.getSettlementStatus().equals("CP")) {
				allSettle += settle.getSettlementAmount();
			}
			if (settle.getSettlementStatus().equals("CP")
					&& !settle.getSettledAt().toLocalDate().isBefore(firstDayOfThisMonth)
					&& !settle.getSettledAt().toLocalDate().isAfter(lastDayOfThisMonth)) {
				thisMonthSettle += settle.getSettlementAmount();
			}
			if (settle.getSettlementStatus().equals("RQ")) {
				todaySettle++;
			}
		}
		// 총 매출
		map.put("allSettle", allSettle);
		// 하루 매출
		map.put("thisMonthSettle", thisMonthSettle);
		// 총 정산 요청건
		map.put("todaySettle", todaySettle);

		// 하루OpenClass
		List<ClassCalendar> calendarList = calendarRepository.findAll();
		int todayCalendarCount = 0;
		for (ClassCalendar cal : calendarList) {
			if (cal.getStatus().equals("모집중") && cal.getStatus().equals("모집마감")
					&& !cal.getStartDate().before(Date.valueOf(currentDate))
					&& !cal.getEndDate().after(Date.valueOf(currentDate))) {
				todayCalendarCount++;
			}
		}
		map.put("todayCalendarCount", todayCalendarCount);
		int todaySignClass = 0;
		for (ClassCalendar cal : calendarList) {
			if (cal.getStatus().equals("승인대기")) {
				todaySignClass++;
			}
		}
		map.put("todaySignClass", todaySignClass);

		// 월별 클래스 개설 추이
		// 12월 모두 포함하는 Map 생성 (순서 유지)
		List<Map<String, Object>> resultMonth = new ArrayList<>();

		for (int i = 1; i <= 12; i++) {
			Map<String, Object> monthData = new HashMap<>();
			String label = String.format("%02d월", i);
			int classCount = getClassCountForMonth(i);
			int studentCount = classCount - getStudentCountForMonth(i);
			int rate = (int) ((double) studentCount / classCount * 100);

			monthData.put("label", label);
			monthData.put("class", classCount);
			monthData.put("student", studentCount);
			monthData.put("rate", rate);

			resultMonth.add(monthData);
		}
		map.put("monthlyStats", resultMonth);
		
		// 분기별 클래스 개설 추이
		List<Map<String,Object>> resultQuater = new ArrayList<>();
		
		for(int i=1; i<=4; i++) {
			Map<String,Object> quaterData = new HashMap<>();
			String label = String.format("%d분기", i);
			int classCount = getClassCountForQuater(i);
			int studentCount = classCount - getStudentCountForQuater(i);
			int rate = (int) ((double) studentCount / classCount * 100);
			quaterData.put("label", label);
			quaterData.put("class", classCount);
			quaterData.put("student", studentCount);
			quaterData.put("rate", rate);
			
			resultQuater.add(quaterData);
		}
		map.put("resultQuater", resultQuater);

		// 년도별 클래스 개설 추이
		List<Map<String, Object>> resultYear = new ArrayList<>();

		for (int i = 2024; i <= 2028; i++) {
			Map<String, Object> yearData = new HashMap<>();
			String label = String.format("%d년", i);
			int classCount = getClassCountForYear(i);
			int studentCount = classCount - getStudentCountForYear(i);
			int rate = (int) ((double) studentCount / classCount * 100);
			yearData.put("label", label);
			yearData.put("class", classCount);
			yearData.put("student", studentCount);
			yearData.put("rate", rate);
			
			resultYear.add(yearData);
		}
		map.put("resultYear", resultYear);
		
		
		
		return map;
	}

	// 특정달에 모든 클래스 수
	public int getClassCountForMonth(int month) {
		List<ClassCalendar> list = calendarRepository.findAll();
		int count = 0;
		for (ClassCalendar cal : list) {
			if (cal.getStartDate().toLocalDate().getMonthValue() == month) {
				count++;
			}
		}
		return count;
	}

	// 특정달에 상태가 검수중,폐강,반려인 클래스들의 수
	public int getStudentCountForMonth(int month) {
		List<ClassCalendar> list = calendarRepository.findAll();
		int count = 0;
		for (ClassCalendar cal : list) {
			if (cal.getStartDate().toLocalDate().getMonthValue() == month && cal.getStatus().equals("검수중")
					&& cal.getStatus().equals("폐강") && cal.getStatus().equals("반려")) {
				count++;
			}
		}
		return count;
	}
	
	//분기별 모든 클래스 수
	public int getClassCountForQuater(int quater) {
		List<ClassCalendar> list = calendarRepository.findAll();
		int count = 0;
		for (ClassCalendar cal : list) {
			if ((cal.getStartDate().toLocalDate().getMonthValue() - 1) / 3+1 == quater) {
				count++;
			}
		}
		return count;
	}
	
	//분기별 특정 클래스의 특정상태의 수
	public int getStudentCountForQuater(int quater) {
		List<ClassCalendar> list = calendarRepository.findAll();
		int count = 0;
		for (ClassCalendar cal : list) {
			if ((cal.getStartDate().toLocalDate().getMonthValue() - 1) / 3+1 == quater && cal.getStatus().equals("검수중")
					&& cal.getStatus().equals("폐강") && cal.getStatus().equals("반려")) {
				count++;
			}
		}
		return count;
	}
	
	//년도별 모든 클래스 수
	public int getClassCountForYear(int year) {
		List<ClassCalendar> list = calendarRepository.findAll();
		int count = 0;
		for (ClassCalendar cal : list) {
			if (cal.getStartDate().toLocalDate().getYear() == year) {
				count++;
			}
		}
		return count;
	}
	
	//분기별 특정 클래스의 특정상태의 수
		public int getStudentCountForYear(int year) {
			List<ClassCalendar> list = calendarRepository.findAll();
			int count = 0;
			for (ClassCalendar cal : list) {
				if (cal.getStartDate().toLocalDate().getYear() == year && cal.getStatus().equals("검수중")
						&& cal.getStatus().equals("폐강") && cal.getStatus().equals("반려")) {
					count++;
				}
			}
			return count;
		}
	
	

}
