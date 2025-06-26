package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.QLikeList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeListRepositoryImpl implements LikeListRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Integer> findFeedIdsByUserId(Integer userId) {
        QLikeList like = QLikeList.likeList;
        return queryFactory
                .select(like.feed.feedId)
                .from(like)
                .where(like.user.userId.eq(userId))
                .fetch();
    }
}

