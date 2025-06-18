package com.dev.moyering.common.repository;

import java.util.List;

import com.dev.moyering.common.dto.SubCategoryDto;

public interface SubCategoryRepositoryCustom {
	List<SubCategoryDto> findSubWithCategory() throws Exception;
}
