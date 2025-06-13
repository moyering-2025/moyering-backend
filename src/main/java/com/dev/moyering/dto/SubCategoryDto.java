package com.dev.moyering.dto;

import com.dev.moyering.entity.Category;
import com.dev.moyering.entity.subCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {

    private Integer subCategoryId;
    private String subCategoryName;
    private Integer categoryId;//1차
    private String categoryName;//1차 카테고리
    public subCategory toEntity() {
    	return subCategory.builder()
    			.subCategoryId(subCategoryId)
    			.subCategoryName(subCategoryName)
    			.firstCategory(
    					Category.builder().
    					categoryId(categoryId).
    					categoryName(categoryName)
    					.build()
				)
    			.build();
    }
}
