package com.dev.moyering.common.repository;

import java.util.List;

import com.dev.moyering.common.entity.Category;
import com.dev.moyering.common.entity.SubCategory;

public interface CategoryRepositoryCustom {
	List<Category> getFirstCategoryList() throws Exception;
	List<SubCategory> getSecondCategoryList(Integer firstCategoryId) throws Exception;
}
