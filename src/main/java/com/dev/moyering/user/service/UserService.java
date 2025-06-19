package com.dev.moyering.user.service;

import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;

public interface UserService {
	void join(UserDto userDto) throws Exception;
	UserDto findUserByUserId(Integer userId) throws Exception;
	User  findUserByUsername(String username) throws Exception;


}
