package com.dev.moyering.common.service;

import java.util.List;

import com.dev.moyering.common.dto.SubCategoryDto;

public interface SubCategoryService {
	List<SubCategoryDto> getSubCategoriesWithParent() throws Exception;
}
