package com.dev.moyering.classring.service;

import com.dev.moyering.classring.dto.ClassLikesDto;

public interface ClassLikesService {
	void toggleLike(ClassLikesDto dto) throws Exception;
}
