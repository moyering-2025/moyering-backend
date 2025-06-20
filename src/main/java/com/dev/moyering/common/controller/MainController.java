package com.dev.moyering.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.admin.service.BannerService;
import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.dto.GatheringSearchRequestDto;
import com.dev.moyering.common.dto.GatheringSearchResponseDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.service.ClassCalendarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {
	private final ClassCalendarService classCalendarService;
	private final GatheringService gatheringService;
	private final BannerService bannerService;
	
    @GetMapping("/main")
    public ResponseEntity<Map<String, Object>> getMainClasses(@AuthenticationPrincipal PrincipalDetails principal) {
        List<HostClassDto> classes;
        List<HostClassDto> hotClasses;
        List<GatheringDto> gathers;
        List<BannerDto> banners;
        Integer userId=null;
		try {
			if (principal != null) {
				 userId = principal.getUser().getUserId();
			}
			
			classes = classCalendarService.getRecommendHostClassesForUser2(userId);
			gathers = gatheringService.getMainGathersForUser(userId);
			
			hotClasses = classCalendarService.getHotHostClasses();
			banners = bannerService.getMainBnanerList(1);
			System.out.println(gathers+"sss");
			Map<String, Object> result = new HashMap<>();
			result.put("classes", classes);
			result.put("hotClasses", hotClasses);
			result.put("gathers", gathers);
			result.put("banners", banners);
			System.out.println("resulr"+result);
	        return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}

    }
    
    @PostMapping("/gatheringList")
    public ResponseEntity<GatheringSearchResponseDto> searchClasses(
            @RequestBody GatheringSearchRequestDto dto) {
    	GatheringSearchResponseDto response;

		try {
			response = gatheringService.searchGathers(dto);
			System.out.println(response+"resresres");
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		}
    }
}
