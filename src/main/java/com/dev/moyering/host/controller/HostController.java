package com.dev.moyering.host.controller;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.dto.CategoryDto;
import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.service.CategoryService;
import com.dev.moyering.common.service.SubCategoryService;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostClassSearchRequestDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import com.dev.moyering.host.dto.ScheduleDetailDto;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.service.ClassCalendarService;
import com.dev.moyering.host.service.ClassCouponService;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.host.service.ScheduleDetailService;
import com.dev.moyering.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
public class HostController {

	@Autowired
	private HostService hostService;
	@Autowired
	private HostClassService hostClassService;
	@Autowired
	private ScheduleDetailService scheduleDetailService;
	@Autowired
	private UserService userService;
	@Autowired
	private ClassCouponService couponService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SubCategoryService subCategoryService;
	@Autowired
	private ClassCalendarService calendarService;
	
	
	@PostMapping("/host/regist")
	public ResponseEntity<Integer> hostRegist(HostDto hostDto,
			@RequestParam(name = "ifile", required = false) MultipartFile profile,
			Integer userId) {
		try {
			System.out.println("호스트DTO:"+hostDto);
			System.out.println("유저아이디:"+userId);
			Integer hostId = hostService.registHost(hostDto, profile);
			System.out.println(hostId);
			userService.updateUserRole(userId);
			return new ResponseEntity<Integer>(hostId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}
	}
	
	@GetMapping("/host/getMyHostInfo")
	public ResponseEntity<Integer> getMyHostInfo(Integer userId){
		try {
			Integer hostId = hostService.findByUserId(userId).getHostId();
			return new ResponseEntity<Integer>(hostId,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<Integer> classRegist(HostClassDto hostClassDto, Date[] dates,@RequestPart("coupons") String couponsJson,
			@RequestPart("scheduleDetail") String scheduleDetail) {
		try {
			System.out.println("호스트:" + hostClassDto);
			Integer classId = hostClassService.registClass(hostClassDto, Arrays.asList(dates));
			System.out.println(hostClassDto.getCategory1());
			System.out.println(hostClassDto.getCategory2());
			scheduleDetailService.registScheduleDetail(scheduleDetail, classId);
			
			 ObjectMapper mapper = new ObjectMapper();
		        mapper.registerModule(new JavaTimeModule()); // LocalDateTime용
		        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		        List<ClassCouponDto> coupons = mapper.readValue(
		                couponsJson,
		                new TypeReference<List<ClassCouponDto>>() {}
		        );
		        for(ClassCouponDto coupon : coupons) {
		        	coupon.setClassId(classId);
		        }
			couponService.insertHostSelectedCoupon(coupons, classId);
			return new ResponseEntity<>(classId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/host/classRegistCategory")
	public ResponseEntity<Map<String,Object>> classRegistCategory(){
		try {
			List<CategoryDto> categoryList = categoryService.getFirstCategoryList();
			List<SubCategoryDto> subCategoryList = subCategoryService.getSubCategoriesWithParent();
			Map<String,Object> map = new HashMap<>();
			map.put("category", categoryList);
			map.put("subCategory", subCategoryList);
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
//	
//	@PostMapping("/host/classRegist/save")
//	public ResponseEntity<Integer> saveTemp(HostClassDto hostClassDto, Date[] dates,@RequestPart("coupons") String couponsJson,
//			@RequestPart("scheduleDetail") String scheduleDetail){
//		try {
//			Integer classId = hostClassService.registClass(hostClassDto, Arrays.asList(dates));
//			System.out.println(hostClassDto.getCategory1());
//			System.out.println(hostClassDto.getCategory2());
//			scheduleDetailService.registScheduleDetail(scheduleDetail, classId);
//			
//			 ObjectMapper mapper = new ObjectMapper();
//		        mapper.registerModule(new JavaTimeModule()); // LocalDateTime용
//		        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//		        List<ClassCouponDto> coupons = mapper.readValue(
//		                couponsJson,
//		                new TypeReference<List<ClassCouponDto>>() {}
//		        );
//			
//			couponService.insertHostSelectedCoupon(coupons, classId);
//			return new ResponseEntity<>(classId, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//	

	
	@PostMapping("/host/class/list")
	public ResponseEntity<HostPageResponseDto<HostClassDto>> getClassListByHostIdWithPagination(
	        @RequestBody HostClassSearchRequestDto dto) {
	    try {
	    	HostPageResponseDto<HostClassDto> response = hostClassService.selectHostClassByHostIdWithPagination(dto);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@GetMapping("/host/hostClassDetail")
	public ResponseEntity<Map<String, Object>> hostClassDetail(@RequestParam Integer classId,
			@RequestParam Integer calendarId, @RequestParam Integer hostId) {
		try {
			Map<String, Object> result = new HashMap<>();
			HostClassDto dto = hostClassService.getClassDetail(classId, calendarId, hostId);
			List<ScheduleDetailDto> scheduleDetails = scheduleDetailService.selectScheduleDetailByClassId(classId);
			List<ClassCouponDto> couponList = couponService.getCouponByClassId(classId);
			System.out.println(classId);
			System.out.println(scheduleDetails);
			result.put("hostClass", dto);
			result.put("scheduleDetail", scheduleDetails);
			result.put("couponList", couponList);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/host/updateHostClassDetail")
	public ResponseEntity<Map<String,Object>> classUpdateDetail(@RequestParam Integer classId){
		try {
			Map<String,Object> result = new HashMap<>();
			HostClassDto dto = hostClassService.getClassDetailByClassID(classId);
			List<ClassCalendarDto> calendarList = calendarService.selectCalednarByClassId(classId);
			System.out.println("캘린다:"+calendarList);
			List<ScheduleDetailDto> scheduleDetails = scheduleDetailService.selectScheduleDetailByClassId(classId);
			List<ClassCouponDto> couponList = couponService.getCouponByClassId(classId);
			result.put("hostClass", dto);
			result.put("scheduleDetail",scheduleDetails);
			result.put("couponList", couponList);
			result.put("calendarList", calendarList);
			System.out.println(calendarList);
			return new ResponseEntity<>(result,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/host/classUpdate/submit")
	public ResponseEntity<Integer> classUpdate(HostClassDto hostClassDto, @RequestPart("coupons") String couponsJson,
			@RequestPart("scheduleDetail") String scheduleDetail) {
		try {
			System.out.println("호스트:" + hostClassDto);
			Integer classId = hostClassService.updateClass(hostClassDto);
			
			
//			scheduleDetailService.registScheduleDetail(scheduleDetail, classId);
//			 ObjectMapper mapper = new ObjectMapper();
//		        mapper.registerModule(new JavaTimeModule()); // LocalDateTime용
//		        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//		        List<ClassCouponDto> coupons = mapper.readValue(
//		                couponsJson,
//		                new TypeReference<List<ClassCouponDto>>() {}
//		        );
//		        for(ClassCouponDto coupon : coupons) {
//		        	coupon.setClassId(classId);
//		        }
//			couponService.insertHostSelectedCoupon(coupons, classId);
			return new ResponseEntity<>(classId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
