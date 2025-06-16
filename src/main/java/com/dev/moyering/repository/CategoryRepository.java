package com.dev.moyering.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev.moyering.entity.common.Category;
import com.dev.moyering.entity.common.QCategory;
import com.dev.moyering.entity.common.QSubCategory;
import com.dev.moyering.entity.common.SubCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CategoryRepository {

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
