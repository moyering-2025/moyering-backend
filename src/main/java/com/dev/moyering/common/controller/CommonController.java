package com.dev.moyering.common.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/filedown")
	public void filedown(@RequestParam("filename") String filename, HttpServletResponse response) {
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("content-Disposition", "attatchment; filename= "+new String(filename.getBytes(),"8859_1"));
			FileInputStream fis = new FileInputStream(new File(duploadPath, filename));
			FileCopyUtils.copy(fis, response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
