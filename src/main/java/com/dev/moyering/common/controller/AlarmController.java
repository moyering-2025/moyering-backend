package com.dev.moyering.common.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.util.PageInfo;
@RestController
public class AlarmController {
	@Autowired
	private AlarmService alarmService;

	@PostMapping("/fcmToken")
	public ResponseEntity<String> fcmToken(@RequestBody Map<String, Object> param) {
		System.out.println(param);
		try {
			alarmService.registFcmToken((Integer)param.get("userId"), (String)param.get("fcmToken"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
//	@PostMapping("/sendAlarm")
//	public ResponseEntity<Boolean> sendAlarm(@RequestBody AlarmDto alarmDto) {
//		Boolean sendSucces = false;
//		try {
//			System.out.println("알람 보내기 테스트");
//			sendSucces = alarmService.sendAlarm(alarmDto);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<Boolean>(sendSucces, HttpStatus.OK);
//	}
	@PostMapping("/confirm/{num}")
	public ResponseEntity<Boolean> confirmAlarm(@PathVariable Integer num) throws Exception {
		Boolean confirm = alarmService.confirmAlarm(num);
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}

	@PostMapping("/confirmAll")
	public ResponseEntity<Boolean> confirmAlarmAll(@RequestBody Map<String, List<Integer>> param) throws Exception {
		System.out.println("param : " +param);
		Boolean confirm = alarmService.confirmAlarmAll(param.get("alarmList"));
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}
	@PostMapping("/user/alarmList")
	public ResponseEntity<Map<String, Object>> alarmList(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody(required=false) Map<String, Object> param){
		System.out.println("로그인 아이디 : "+principal.getUser().getUserId());
		Integer loginId = principal.getUser().getUserId();
		Map<String, Object> res = new HashMap<>();
		PageInfo pageInfo = new PageInfo(1);
		Integer alarmCnt = 0;
	    Integer alarmType = null;
	    Date startDate = null;
	    Date endDate = null;
	    Boolean isConfirmed = null;
	    if(param !=null ) {
	    	if(param.get("page")!=null) {
				pageInfo.setCurPage((Integer)param.get("page"));
			}
            if (param.get("isConfirmed")!=null) {
            	isConfirmed = (Boolean) param.get("isConfirmed");
            }
		    alarmType = (Integer) param.get("alarmType");
			if(param.get("startDate") != null) {
                String startDateStr = (String) param.get("startDate");
                if (!startDateStr.trim().isEmpty()) {
                    startDate = Date.valueOf(startDateStr); 
                }
            }
            if(param.get("endDate") != null) {
                String endDateStr = (String) param.get("endDate");
                if (!endDateStr.trim().isEmpty()) {
                    endDate = Date.valueOf(endDateStr);
                }
            }
	    }
		List<AlarmDto> alarmList = null;
		try {
			alarmCnt = alarmService.countAlarmsByReceiverUserId(loginId, alarmType, startDate, endDate, isConfirmed).intValue();
			res.put("alarmCnt", alarmCnt);
			if(alarmCnt > 0) {
				alarmList = alarmService.findAlarmListByReceiverUserId(pageInfo, loginId, alarmType, startDate, endDate, isConfirmed);		
				res.put("alarmList", alarmList);
				res.put("pageInfo", pageInfo);
			}
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/user/alarms")
	public ResponseEntity<List<AlarmDto>> getAlarms(@AuthenticationPrincipal PrincipalDetails principal) throws Exception {
		Integer loginId = principal.getUser().getUserId();
		try {
			List<AlarmDto> alarms =  alarmService.getAlarmList(loginId);
			return new ResponseEntity<>(alarms, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
