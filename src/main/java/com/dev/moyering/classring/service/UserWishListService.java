package com.dev.moyering.classring.service;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;
import com.dev.moyering.common.dto.PageResponseDto;

public interface UserWishListService {
	PageResponseDto<WishlistItemDto> getWishlist(UtilSearchDto dto) throws Exception;

}
