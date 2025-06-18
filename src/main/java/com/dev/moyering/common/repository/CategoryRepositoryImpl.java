package com.dev.moyering.common.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev.moyering.common.entity.Category;
import com.dev.moyering.common.entity.QCategory;
import com.dev.moyering.common.entity.QSubCategory;
import com.dev.moyering.common.entity.SubCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public List<Category> getFirstCategoryList() throws Exception {
		QCategory category = QCategory.category;
		return jpaQueryFactory.selectFrom(category)
				.orderBy(category.categoryId.asc())
				.fetch();
	}
	public List<SubCategory> getSecondCategoryList(Integer firstCategoryId) throws Exception {
		QSubCategory subCategory = QSubCategory.subCategory;
		return jpaQueryFactory.selectFrom(subCategory)
				.where((subCategory.firstCategory.categoryId).eq(firstCategoryId))
				.orderBy(subCategory.subCategoryId.asc())
				.fetch();
	}
}
