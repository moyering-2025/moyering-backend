package com.dev.moyering.classring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;

public interface ClassLikesRepositoryCustom {
	Page<WishlistItemDto> searchByUser(UtilSearchDto dto, Pageable pageable) throws Exception;
}
