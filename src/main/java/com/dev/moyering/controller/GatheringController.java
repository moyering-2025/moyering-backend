package com.dev.moyering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.dto.user.GatheringDto;
import com.dev.moyering.service.GatheringService;

@RestController 
public class GatheringController {
	@Autowired
	private GatheringService gatheringService;
	
	@PostMapping("/user/writeGathering")
	public ResponseEntity<GatheringDto> write(GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail", required=false) MultipartFile thumbnail) {
		try {
			System.out.println("gatheringDto : "+gatheringDto +", "+thumbnail);
//			Integer gatheringId = gatheringService.writeGathering(gatheringDto, thumbnail);
//			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
//			return new ResponseEntity<>(nGatheringDto, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return null;
	}
}
