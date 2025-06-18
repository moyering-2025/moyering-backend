package com.dev.moyering.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {

}
