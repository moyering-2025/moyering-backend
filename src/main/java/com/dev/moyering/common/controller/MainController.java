package com.dev.moyering.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.admin.service.BannerService;
import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.service.ClassCalendarService;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {
	private final HostClassService hostClassService;
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
			System.out.println(principal+"principalprincipalprincipal");
			if (principal != null) {
				userId = principal.getUser().getUserId();
				System.out.println(userId+"idididididi");
				classes = hostClassService.getRecommendHostClassesForUser(userId);
				gathers = gatheringService.getMainGathersForUser(userId);
			} else {
				classes = hostClassService.getRecommendHostClassesForUser(null);
				gathers = gatheringService.getMainGathersForUser(null);
			}
			
			hotClasses = classCalendarService.getHotHostClasses();
			banners = bannerService.getMainBnanerList(1);
			System.out.println(classes+"sss");
			Map<String, Object> result = new HashMap<>();
			result.put("classes", classes);
			result.put("hotClasses", hotClasses);
			result.put("gathers", hotClasses);
			result.put("banners", banners);

	        return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build(); // 500 에러 응답
		}

    }
}
