package com.dev.moyering.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.CategoryDto;
import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.entity.Category;
import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.common.repository.CategoryRepository;
@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private CategoryRepository categoryRepository;
//	public List<CategoryDto> getFirstCategoryList() throws Exception {
//		List<Category> firstCategoryList = categoryRepository.getFirstCategoryList();
//		return firstCategoryList.stream().map(c->c.toDto()).collect(Collectors.toList());
//	}
//	public List<SubCategoryDto> getSecondCategoryList(Integer firstCategoryId) throws Exception {
//		List<SubCategory> secondCategoryList = categoryRepository.getSecondCategoryList(firstCategoryId);
//		return secondCategoryList.stream().map(sc->sc.toDto()).collect(Collectors.toList());
//	}

	@Override
	public List<CategoryDto> getFirstCategoryList() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubCategoryDto> getSecondCategoryList(Integer firstCategoryId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
