package com.dev.moyering.host.controller;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.HostService;

@RestController
public class HostController {
	
	@Autowired
	private HostService hostService;
	@Autowired
	private HostClassService hostClassService;
	
	
	@PostMapping("/host/regist")
	public ResponseEntity<Integer> hostRegist(HostDto hostDto,
			@RequestParam(name="ifile",required=false) MultipartFile profile){
		try {
			Integer hostId = hostService.registHost(hostDto,profile);
			System.out.println(hostId);
			return new ResponseEntity<Integer>(hostId,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		}
	}
	
	@PostMapping("/host/classRegist/submit")
	public ResponseEntity<Integer> classRegist(HostClassDto hostClassDto, Date[] dates){
		try {
			Integer classId = hostClassService.registClass(hostClassDto, Arrays.asList(dates));
			return new ResponseEntity<>(classId, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/host/HostclassList")
	public ResponseEntity<Map<Integer,List<ClassCalendarDto>>> hostClassList(@RequestBody Integer hostId){
		try {
			Map<Integer,List<ClassCalendarDto>> results = hostClassService.getHostClassesWithCalendars(hostId);
			return new ResponseEntity<>(results,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.OK);
		}
	}
	
	

}
