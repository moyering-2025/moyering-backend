package com.dev.moyering.common.service;

import java.util.List;

import com.dev.moyering.common.dto.CategoryDto;
import com.dev.moyering.common.dto.SubCategoryDto;

public interface CategoryService {
	public List<CategoryDto> getFirstCategoryList() throws Exception;
	public List<SubCategoryDto> getSecondCategoryList(Integer firstCategoryId) throws Exception;
}
