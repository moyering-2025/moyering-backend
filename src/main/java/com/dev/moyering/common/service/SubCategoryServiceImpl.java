package com.dev.moyering.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.repository.SubCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

	@Override
	public List<SubCategoryDto> getSubCategoriesWithParent() throws Exception {
		return subCategoryRepository.findSubWithCategory();
	}

}
