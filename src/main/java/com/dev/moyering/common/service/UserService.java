package com.dev.moyering.common.service;

import com.dev.moyering.common.dto.UserDto;

public interface UserService {

	UserDto findUserByUserId(Integer userId) throws Exception;

}
