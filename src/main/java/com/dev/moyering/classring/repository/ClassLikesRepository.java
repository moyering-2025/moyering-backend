package com.dev.moyering.classring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.classring.entity.ClassLikes;

public interface ClassLikesRepository extends JpaRepository<com.dev.moyering.classring.entity.ClassLikes, Integer> , ClassLikesRepositoryCustom{

	Optional<ClassLikes> findByUser_UserIdAndHostClass_ClassId(Integer userId, Integer classId) throws Exception;
	List<ClassLikes> findAllByUser_userId(Integer userId) throws Exception;

}
