package com.dev.moyering.common.dto;

import com.dev.moyering.common.entity.Category;
import com.dev.moyering.common.entity.SubCategory;

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
    public SubCategory toEntity() {
    	return SubCategory.builder()
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
