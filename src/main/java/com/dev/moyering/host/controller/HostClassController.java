package com.dev.moyering.host.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.service.HostClassService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HostClassController {
    private final HostClassService hostClassService;

    @PostMapping("/classList")
    public ResponseEntity<PageResponseDto<HostClassDto>> searchClasses(
            @RequestBody ClassSearchRequestDto dto) {
    	PageResponseDto<HostClassDto> response;

		try {
			response = hostClassService.searchClasses(dto);
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
    
    @GetMapping("/host/calendar")
    public ResponseEntity<List<HostClassDto>> selectClassCalendar(@RequestParam Integer hostId){
    	try {
    		List<HostClassDto> hostClasses = hostClassService.selectHostClassByHostId(hostId);
    		return new ResponseEntity<>(hostClasses,HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
}
