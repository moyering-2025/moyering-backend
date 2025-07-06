package com.dev.moyering.classring.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;
import com.dev.moyering.classring.repository.ClassLikesRepository;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.gathering.repository.GatheringLikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserWishListServiceImpl implements UserWishListService {
	private final ClassLikesRepository classLikesRepository;
	private final GatheringLikesRepository gatheringLikesRepository;
	@Override
	public PageResponseDto<WishlistItemDto> getWishlist(UtilSearchDto dto) throws Exception {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by("id").descending());
        Page<WishlistItemDto> pageResult = "classRing".equals(dto.getTab())
            ? classLikesRepository.searchByUser(dto, pageable)
            : gatheringLikesRepository.searchByUser(dto, pageable);

        return PageResponseDto.<WishlistItemDto>builder()
                .content(pageResult.getContent())
                .currentPage(pageResult.getNumber() + 1)
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .build();
	}

}
