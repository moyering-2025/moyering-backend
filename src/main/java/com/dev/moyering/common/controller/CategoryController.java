package com.dev.moyering.common.controller;

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

import com.dev.moyering.common.dto.CategoryDto;
import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.service.CategoryService;
import com.dev.moyering.common.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
	private final CategoryService categoryService;
	private final SubCategoryService subCategoryService;

	@GetMapping("/category")
	public ResponseEntity<Map<String,Object>> getFirstCategoryList() {
		try {
			List<CategoryDto> firstCategoryList = categoryService.getFirstCategoryList();
			Map<String, Object> res = new HashMap<>();
			res.put("category", firstCategoryList);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/subCategory/{categoryId}")
	public ResponseEntity<Map<String,Object>> getSecondCategoryList(@PathVariable("categoryId") Integer categoryId) {
	    try {
	        List<SubCategoryDto> secondCategoryList = categoryService.getSecondCategoryList(categoryId);
	        Map<String, Object> res = new HashMap<>();
	        res.put("subCategory", secondCategoryList);
	        return new ResponseEntity<>(res, HttpStatus.OK);
	    } catch(Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}

	// admin 카테고리 관리
//	@GetMapping("/admin/category")
//	public ResponseEntity<List<SubCategoryDto>> getAllCategoriesForAdmin() {
//		try {
//			List<SubCategoryDto> allCategories = categoryService.getFirstCategoryList();
//			return new ResponseEntity<>(allCategories, HttpStatus.OK);
//		} catch(Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
	
    @GetMapping("/categories/suball")
    public ResponseEntity<List<SubCategoryDto>> getSubAll() {
        try {
            List<SubCategoryDto> result = subCategoryService.getSubCategoriesWithParent();
            return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
		}
    }
}
