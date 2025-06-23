package com.dev.moyering.host.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.service.HostClassService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HostClassController {
    private final HostClassService hostClassService;

    @PostMapping("/classList")
    public ResponseEntity<ClassSearchResponseDto> searchClasses(
            @RequestBody ClassSearchRequestDto dto) {
        ClassSearchResponseDto response;
		System.out.println(dto+"dtodtodtodto");

		try {
			response = hostClassService.searchClasses(dto);
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
}
