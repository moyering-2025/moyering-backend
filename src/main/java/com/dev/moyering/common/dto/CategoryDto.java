package com.dev.moyering.common.dto;

import com.dev.moyering.common.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer categoryId;
    private String categoryName;
    public Category toEntity() {
    	return Category.builder()
    			.categoryId(categoryId)
    			.categoryName(categoryName)
    			.build();
    }
}