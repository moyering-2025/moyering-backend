package com.dev.moyering.service;

import java.util.List;

import com.dev.moyering.dto.common.CategoryDto;
import com.dev.moyering.dto.common.SubCategoryDto;

public interface CategoryService {
	public List<CategoryDto> getFirstCategoryList() throws Exception;
	public List<SubCategoryDto> getSecondCategoryList(Integer firstCategoryId) throws Exception;
}
