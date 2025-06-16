package com.dev.moyering.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dev.moyering.common.dto.SubCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamicInsert
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subCategoryId;
    
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="categoryId")
	private Category firstCategory;
	@Column
    private String subCategoryName;
	public SubCategoryDto toDto() {
		return SubCategoryDto.builder()
				.subCategoryId(subCategoryId)
				.subCategoryName(subCategoryName)
				.categoryId(firstCategory.getCategoryId())
				.categoryName(firstCategory.getCategoryName())
				.build();
	}
}
