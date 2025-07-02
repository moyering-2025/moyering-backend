package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.QFollow;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.types.Projections;

import java.util.List;

public interface FollowRepositoryCustom {
    List<UserDto> findFollowersWithPaging(Integer followingId, int offset, int size, String search);

    List<UserDto> findFollowingsWithPaging(Integer followingId, int offset, int size, String search);
}
