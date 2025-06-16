package com.dev.moyering.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.dto.common.CategoryDto;
import com.dev.moyering.dto.common.SubCategoryDto;
import com.dev.moyering.service.CategoryService;

@RestController 
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
	@Autowired 
	private CategoryService categoryService;

	@GetMapping("/category1")
	public ResponseEntity<Map<String,Object>> getFirstCategoryList() {
		try {
			List<CategoryDto> firstCategoryList = categoryService.getFirstCategoryList();
			Map<String, Object> res = new HashMap<>();
			res.put("category1", firstCategoryList);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/category2/{category1Id}")
	public ResponseEntity<Map<String,Object>> getSecondCategoryList(@PathVariable("category1Id") Integer category1Id) {
	    try {
	        List<SubCategoryDto> secondCategoryList = categoryService.getSecondCategoryList(category1Id);
	        Map<String, Object> res = new HashMap<>();
	        res.put("category2", secondCategoryList);
	        return new ResponseEntity<>(res, HttpStatus.OK);
	    } catch(Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
}
