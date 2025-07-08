package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.QFeed;
import com.dev.moyering.socialing.entity.QLikeList;
import com.dev.moyering.user.entity.QUserBadge;
import com.querydsl.core.types.Projections;
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

    @Override
    public List<FeedDto> findAllWithLikedByUser(Integer userId) {
        QFeed feed = QFeed.feed;
        QLikeList likeList = QLikeList.likeList;
        QUserBadge badge = QUserBadge.userBadge;

        return queryFactory
                .select(Projections.constructor(FeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1,
                        feed.img2,
                        feed.img3,
                        feed.img4,
                        feed.img5,
                        feed.tag1,
                        feed.tag2,
                        feed.tag3,
                        feed.tag4,
                        feed.tag5,
                        feed.isDeleted,
                        feed.user.nickName,
                        feed.user.profile,
                        feed.user.userBadgeId,
                        feed.createDate,
                        likeList.likeid.isNotNull(), // 좋아요 눌렀으면 true
                        badge.badge_img
                ))
                .from(feed)
                .leftJoin(likeList)
                .on(likeList.feed.feedId.eq(feed.feedId)
                        .and(likeList.user.userId.eq(userId)))
                .leftJoin(badge)
                .on(badge.user.eq(feed.user)
                        .and(badge.isRepresentative.eq(true)))
                .fetch();
    }
}
