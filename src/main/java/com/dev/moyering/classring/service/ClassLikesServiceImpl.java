package com.dev.moyering.classring.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.classring.dto.ClassLikesDto;
import com.dev.moyering.classring.entity.ClassLikes;
import com.dev.moyering.classring.repository.ClassLikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassLikesServiceImpl implements ClassLikesService {
	private final ClassLikesRepository classLikesRepository;

    @Transactional
	@Override
	public void toggleLike(ClassLikesDto dto) throws Exception {
    	// 이미 좋아요가 있으면 삭제, 없으면 생성
    	classLikesRepository.findByUser_UserIdAndHostClass_ClassId(dto.getUserId(), dto.getClassId())
            .ifPresentOrElse(
            		classLikesRepository::delete,
                () -> {
                    ClassLikes like = dto.toEntity();
                    classLikesRepository.save(like);
                }
            );	
    }

	@Override
	public List<ClassLikesDto> getClasslikeListByUserId(Integer userId) throws Exception {
		return classLikesRepository.findAllByUser_userId(userId).stream()
				.map(cl-> cl.toDto()).collect(Collectors.toList());
	}
}
