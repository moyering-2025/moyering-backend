package com.dev.moyering.classring.service;

import java.util.List;

import com.dev.moyering.classring.dto.ClassLikesDto;

public interface ClassLikesService {
	void toggleLike(ClassLikesDto dto) throws Exception;
	List<ClassLikesDto> getClasslikeListByUserId(Integer userId) throws Exception;

}
