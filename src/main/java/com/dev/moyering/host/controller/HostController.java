package com.dev.moyering.host.controller;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ScheduleDetailDto;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.host.service.ScheduleDetailService;

@RestController
public class HostController {

	@Autowired
	private HostService hostService;
	@Autowired
	private HostClassService hostClassService;
	@Autowired
	private ScheduleDetailService scheduleDetailService;

	@PostMapping("/host/regist")
	public ResponseEntity<Integer> hostRegist(HostDto hostDto,
			@RequestParam(name = "ifile", required = false) MultipartFile profile) {
		try {
			Integer hostId = hostService.registHost(hostDto, profile);
			System.out.println(hostId);
			return new ResponseEntity<Integer>(hostId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/host/hostProfile")
	public ResponseEntity<Host> hostProfile(@RequestParam Integer hostId) {
		try {
			Host host = hostService.findByHostId(hostId);
			return new ResponseEntity<>(host, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/hostProfileUpdate")
	public ResponseEntity<Host> hostProfileUpdate(@RequestParam Integer hostId,
			@RequestParam(required = false) String name, @RequestParam(required = false) String publicTel,
			@RequestParam(required = false) String email, @RequestParam(required = false) String intro,
			@RequestParam(required = false) MultipartFile profile) {
		try {
			Host host = hostService.updateHost(hostId, name, publicTel, email, intro, profile);
			return new ResponseEntity<>(host, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/settlementInfoUpdate")
	public ResponseEntity<Host> hostSettlementInfoUpdate(@RequestParam Integer hostId,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) String accName,
			@RequestParam(required = false) String accNum,
			@RequestParam(value = "idCard", required = false) MultipartFile idCard) {
		try {
			Host host = hostService.updateHostSettlement(hostId, bankName, accName, accNum, idCard);
			return new ResponseEntity<>(host, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/host/classRegist/submit")
	public ResponseEntity<Integer> classRegist(HostClassDto hostClassDto, Date[] dates,
			@RequestPart("scheduleDetail") String scheduleDetail) {
		try {
			Integer classId = hostClassService.registClass(hostClassDto, Arrays.asList(dates));
			System.out.println(hostClassDto.getCategory1());
			System.out.println(hostClassDto.getCategory2());
			scheduleDetailService.registScheduleDetail(scheduleDetail, classId);
			return new ResponseEntity<>(classId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/host/HostclassList")
	public ResponseEntity<List<HostClassDto>> hostClassList(@RequestParam Integer hostId) {
//		
		try {
			List<HostClassDto> hostClasses = hostClassService.selectHostClassByHostId(hostId);
			System.out.println(hostClasses);
			return new ResponseEntity<>(hostClasses, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/host/hostClassDetail")
	public ResponseEntity<Map<String, Object>> hostClassDetail(@RequestParam Integer classId,
			@RequestParam Integer calendarId, @RequestParam Integer hostId) {
		try {
			Map<String, Object> result = new HashMap<>();
			HostClassDto dto = hostClassService.getClassDetail(classId, calendarId, hostId);
			List<ScheduleDetailDto> scheduleDetails = scheduleDetailService.selectScheduleDetailByClassId(classId);
			System.out.println(classId);
			System.out.println(scheduleDetails);
			result.put("hostClass", dto);
			result.put("scheduleDetail", scheduleDetails);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
