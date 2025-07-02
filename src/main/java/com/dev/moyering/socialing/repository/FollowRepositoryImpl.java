package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.QFollow;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserDto> findFollowersWithPaging(Integer followingId, int offset, int size, String search) {

        QFollow follow = QFollow.follow;
        QUser user = QUser.user;

        List<UserDto> result =  jpaQueryFactory
                .select(Projections.constructor(UserDto.class,
                        user.userId,
                        user.username,
                        user.nickName,
                        user.profile
                ))
                .from(follow)
                .join(follow.follower, user)
                .where(
                        follow.following.userId.eq(followingId),
                        search != null && !search.isEmpty()
                                ? user.nickName.containsIgnoreCase(search)
                                : null
                )
//                .orderBy(user.userId.desc())
                .offset(offset)
                .limit(size)
                .fetch();
//        System.out.println("DB 호출: offset=" + offset + ", size=" + size + ", 반환 row 수=" + result.size());
        return result;
    }

    @Override
    public List<UserDto> findFollowingsWithPaging(Integer followerId, int offset, int size, String search) {
        QFollow follow = QFollow.follow;
        QUser user = QUser.user;

        List<UserDto> result =  jpaQueryFactory
                .select(Projections.constructor(UserDto.class,
                        user.userId,
                        user.username,
                        user.nickName,
                        user.profile
                ))
                .from(follow)
                .join(follow.following, user)
                .where(
                        follow.follower.userId.eq(followerId),
                        search != null && !search.isEmpty()
                                ? user.nickName.containsIgnoreCase(search)
                                : null
                )
//                .orderBy(user.userId.desc())
                .offset(offset)
                .limit(size)
                .fetch();
//        System.out.println("DB 호출: offset=" + offset + ", size=" + size + ", 반환 row 수=" + result.size());
        return result;
    }
}
