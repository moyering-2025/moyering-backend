package com.dev.moyering.admin.dto;

import com.dev.moyering.common.entity.Category;
import com.dev.moyering.common.entity.SubCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자, 빈 객체 생성 차단
@Entity
@ToString
public class CategoryDto {
    @Id
    private Integer subCategoryId; // 2차 카테고리 ID(PK)
    private String category1; // 1차 카테고리명
    private String category2; // 2차 카테고리명
    private Integer categoryId; //1차 카테고리 id
    private Integer sortOrder; //정렬 순서
    private Boolean isHidden; //카테고리 숨김 여부

    // 객체 생성
    public Category toCategory() {
        return Category.builder()
                .categoryName(this.category1) // 카테고리
                .build();
    }

    public SubCategory toSubCategory(Category category) {
        return SubCategory.builder()
                .firstCategory(category)
                .subCategoryName(this.category2) // 서브 카테고리
                .build();
    }
}