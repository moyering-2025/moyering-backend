package com.dev.moyering.common.controller;

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
	@PostMapping("/sendAlarm")
	public ResponseEntity<Boolean> sendAlarm(@RequestBody AlarmDto alarmDto) {
		System.out.println(alarmDto);
		Boolean sendSucces = false;
		try {
			sendSucces = alarmService.sendAlarm(alarmDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(sendSucces, HttpStatus.OK);
	}


	@GetMapping("/confirm/{num}")
	public ResponseEntity<Boolean> confirmAlarm(@PathVariable Integer num) throws Exception {
		Boolean confirm = alarmService.confirmAlarm(num);
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}

	@PostMapping("/confirmAll")
	public ResponseEntity<Boolean> confirmAlarmAll(@RequestBody Map<String, List<Integer>> param) throws Exception {
		System.out.println(param);
		Boolean confirm = alarmService.confirmAlarmAll(param.get("alarmList"));
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}
	@GetMapping("/user/alarmList")
	public ResponseEntity<List<AlarmDto>> alarmList(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody(required=false) Map<String, Object> param){
//		return new ResponseEntity<List<AlarmDto>>(fcmMessageService.getAlarmList(principal.getUser().getUserId()),HttpStatus.OK);

		PageInfo pageInfo = new PageInfo(1);
		param.put("loginId", principal.getUser().getUserId());
		List<AlarmDto> alarmList = null;
		try {
			alarmList = alarmService.findAlarmListByReceiverUserId(pageInfo, param);
			return new ResponseEntity<List<AlarmDto>>(alarmList, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
