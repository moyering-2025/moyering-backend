package com.dev.moyering.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CommonController {

	@Value("${iupload.path}")
	private String iuploadPath;

	@Value("${dupload.path}")
	private String duploadPath;

	@RequestMapping("/image")
	public void imageView(@RequestParam("filename") String filename, HttpServletResponse response) {
		try {
			FileInputStream fis = new FileInputStream(new File(iuploadPath, filename));
			FileCopyUtils.copy(fis, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/filedown")
	public void filedown(@RequestParam("filename") String filename, HttpServletResponse response) {
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("content-Disposition",
					"attatchment; filename= " + new String(filename.getBytes(), "8859_1"));
			FileInputStream fis = new FileInputStream(new File(duploadPath, filename));
			FileCopyUtils.copy(fis, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@PostMapping("/api/upload/image")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
		if (image.isEmpty()) {
			return ResponseEntity.badRequest().body("No file selected");
		}

		try {
			// 고유한 파일명 생성
			String originalFilename = image.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String savedName = UUID.randomUUID().toString() + extension;

			// 저장할 파일 객체 생성
			File dest = new File(iuploadPath, savedName);
			image.transferTo(dest);

			// 접근 가능한 URL 생성 (정적 리소스 매핑을 기준으로)
			String imageUrl = "/iupload/" + savedName;

			Map<String, String> result = new HashMap<>();
			result.put("url", imageUrl);

			return ResponseEntity.ok(result);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("File upload failed");
		}
	}

}
