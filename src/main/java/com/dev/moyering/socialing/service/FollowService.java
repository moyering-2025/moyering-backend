package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.FollowDto;
import com.dev.moyering.user.dto.UserDto;

import java.util.List;

public interface FollowService {

    FollowDto follow(Integer followerId, Integer followingId) throws Exception;

    void unfollow(Integer followerId, Integer followingId) throws Exception;

    boolean isFollowing(Integer followerId, Integer followingId);

    List<FollowDto> getFollowings(Integer followerId);

    List<UserDto> getFollowers(Integer followingId,Integer pagem,Integer size,String search);

    List<UserDto> getFollowings(Integer followerId,Integer pagem,Integer size,String search);

    List<Integer> getFollowingUserIds(Integer followerId) throws Exception;
}
