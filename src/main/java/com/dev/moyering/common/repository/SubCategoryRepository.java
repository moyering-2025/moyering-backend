package com.dev.moyering.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.common.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer>,SubCategoryRepositoryCustom {

}
