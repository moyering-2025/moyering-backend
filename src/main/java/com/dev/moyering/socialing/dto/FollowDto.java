package com.dev.moyering.socialing.dto;

import com.dev.moyering.socialing.entity.Follow;
import com.dev.moyering.user.entity.User;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class FollowDto {

    private Integer id;

    private Integer followerId;
    private Integer followingId;


    public Follow toentity() {
        Follow entity = Follow.builder()
                .id(id)
                .follower(User.builder().userId(followerId).build())
                .following(User.builder().userId(followingId).build())
                .build();
        return entity;
    }
}
