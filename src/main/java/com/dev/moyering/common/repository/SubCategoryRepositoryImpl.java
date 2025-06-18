package com.dev.moyering.common.repository;

import java.util.List;

import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.entity.QCategory;
import com.dev.moyering.common.entity.QSubCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubCategoryRepositoryImpl implements SubCategoryRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	@Override
	public List<SubCategoryDto> findSubWithCategory() throws Exception {
		QSubCategory sub = QSubCategory.subCategory;
		QCategory cat = QCategory.category;
		
		return jpaQueryFactory
				.select(Projections.constructor(
						SubCategoryDto.class,
						sub.subCategoryId,
						sub.subCategoryName,
						cat.categoryId,
						cat.categoryName
						))
				.from(sub)
				.join(sub.firstCategory, cat)
				.fetch();
	}

}
