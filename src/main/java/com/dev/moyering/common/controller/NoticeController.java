package com.dev.moyering.common.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.service.NoticeService;
import com.dev.moyering.util.PageInfo;

@RestController
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	@PostMapping("/noticeList")
	public ResponseEntity<Map<String, Object>> getNoticeList(@RequestBody(required=false) Map<String, Object> param) throws Exception {
		System.out.println("param : "+param);
		PageInfo pageInfo = new PageInfo(1);
	    if( param!=null && param.get("page")!=null) {
	    	pageInfo.setCurPage((Integer)param.get("page"));
	    }
	    Map<String, Object> res = new HashMap<>();
		try {
			List<AdminNoticeDto> noticeList = noticeService.findNoticesByPage(pageInfo);
			res.put("alarmList", noticeList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/detailNotice")
	public ResponseEntity<Map<String,Object>> detailNotice(@RequestParam("noticeId") Integer noticeId) {
		try {
			AdminNoticeDto detailNotice = noticeService.findNoticeByNoticeId(noticeId);
		    Map<String, Object> res = new HashMap<>();
		    res.put("result", detailNotice);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
