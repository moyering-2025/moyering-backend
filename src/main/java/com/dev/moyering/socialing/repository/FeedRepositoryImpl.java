package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.QComment;
import com.dev.moyering.socialing.entity.QFeed;
import com.dev.moyering.socialing.entity.QFollow;
import com.dev.moyering.socialing.entity.QLikeList;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedDto> findAllWithCounts() {
        QFeed feed = QFeed.feed;
        QComment comment = QComment.comment;
        QLikeList feedLike = QLikeList.likeList;
        QUser user = QUser.user;

        return jpaQueryFactory
                .select(Projections.fields(FeedDto.class,
                        feed.feedId.as("feedId"),
                        feed.content,
                        feed.img1, feed.img2, feed.img3, feed.img4, feed.img5,
                        feed.tag1, feed.tag2, feed.tag3, feed.tag4, feed.tag5,
                        feed.isDeleted,
                        user.username.as("writerId"),
                        user.profile.as("writerProfile"),
                        user.userBadgeId.as("writerBadge"),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.feed.feedId.eq(feed.feedId)),
                                "commentsCount"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(feedLike.count())
                                        .from(feedLike)
                                        .where(feedLike.feed.feedId.eq(feed.feedId)),
                                "likesCount"
                        )
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .where(feed.isDeleted.eq(false))
                .fetch();
    }

    @Override
    public List<FeedDto> findFeeds(String sortType, Integer userId) {
        QFeed feed = QFeed.feed;
        QComment comment = QComment.comment;
        QLikeList likeList = QLikeList.likeList;
        QUser user = QUser.user;
        QFollow follow = QFollow.follow;

        return jpaQueryFactory
                .select(Projections.fields(FeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1, feed.img2, feed.img3, feed.img4, feed.img5,
                        feed.tag1, feed.tag2, feed.tag3, feed.tag4, feed.tag5,
                        feed.isDeleted,
                        user.userId,
                        user.username.as("writerId"),
                        user.profile.as("writerProfile"),
                        user.userBadgeId.as("writerBadge"),
                        comment.countDistinct().as("commentsCount"),
                        likeList.countDistinct().as("likesCount"),

                        // 로그인 유저가 해당 피드에 좋아요 눌렀는지 여부
                        ExpressionUtils.as(
//                                JPAExpressions.selectOne()
//                                        .from(likeList)
//                                        .where(
//                                                likeList.feed.feedId.eq(feed.feedId)
//                                                        .and(likeList.user.userId.eq(userId))
//                                        )
//                                        .exists(),
//                                "likedByUser"
                                new CaseBuilder()
                                        .when(
                                                JPAExpressions
                                                        .selectOne()
                                                        .from(likeList)
                                                        .where(
                                                                likeList.feed.feedId.eq(feed.feedId)
                                                                        .and(likeList.user.userId.eq(userId))
                                                        )
                                                        .exists()
                                        )
                                        .then(true)
                                        .otherwise(false),
                                "likedByUser"
                        )
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(comment).on(comment.feed.feedId.eq(feed.feedId))
                .leftJoin(likeList).on(likeList.feed.feedId.eq(feed.feedId))
                .where(feed.isDeleted.eq(false)
                        .and(
                                "follow".equals(sortType) && userId != null
                                        ? feed.user.userId.in(
                                        JPAExpressions.select(follow.following.userId)
                                                .from(follow)
                                                .where(follow.follower.userId.eq(userId))
                                )
                                        : null
                        )
                )
                .groupBy(feed.feedId)
                .orderBy(getSortOrder(sortType, comment, likeList, feed))
                .fetch();
    }

    @Override
    public List<String> findTop3Img1ByUserId(Integer userId) {
        QFeed f = QFeed.feed;
        return jpaQueryFactory
                .select(f.img1)
                .from(f)
                .where(f.user.userId.eq(userId)
                        .and(f.isDeleted.isFalse()))
                .orderBy(f.createDate.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<FeedDto> findFeedsWithoutLiked() {
        QFeed feed = QFeed.feed;
        QComment comment = QComment.comment;
        QLikeList likeList = QLikeList.likeList;
        QUser user = QUser.user;

        return jpaQueryFactory
                .select(Projections.fields(FeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1, feed.img2, feed.img3, feed.img4, feed.img5,
                        feed.tag1, feed.tag2, feed.tag3, feed.tag4, feed.tag5,
                        feed.isDeleted,
                        user.userId,
                        user.username.as("writerId"),
                        user.profile.as("writerProfile"),
                        user.userBadgeId.as("writerBadge"),
                        comment.countDistinct().as("commentsCount"),
                        likeList.countDistinct().as("likesCount")
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(comment).on(comment.feed.feedId.eq(feed.feedId))
                .leftJoin(likeList).on(likeList.feed.feedId.eq(feed.feedId))
                .where(feed.isDeleted.eq(false))
                .groupBy(feed.feedId)
                .orderBy(feed.createDate.desc()) // 기본 정렬
                .fetch();
    }

    @Override
    public List<FeedDto> findTopLikedFeeds(int offset, int size) {
        QFeed feed = QFeed.feed;
        QLikeList likeList = QLikeList.likeList;
        QUser user = QUser.user;

        return jpaQueryFactory
                .select(Projections.fields(FeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1, feed.img2, feed.img3, feed.img4, feed.img5,
                        feed.tag1, feed.tag2, feed.tag3, feed.tag4, feed.tag5,
                        feed.isDeleted,
                        user.nickName.as("writerId"),
                        user.profile.as("writerProfile"),
                        user.userBadgeId.as("writerBadge"),
                        likeList.countDistinct().as("likesCount")
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(likeList).on(likeList.feed.feedId.eq(feed.feedId))
                .where(feed.isDeleted.eq(false))
                .groupBy(feed.feedId)
                .orderBy(likeList.countDistinct().desc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    @Override
    public List<FeedDto> findFeedsWithoutLiked(String sortType) {
        QFeed feed = QFeed.feed;
        QComment comment = QComment.comment;
        QLikeList likeList = QLikeList.likeList;
        QUser user = QUser.user;

        return jpaQueryFactory
                .select(Projections.fields(FeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1, feed.img2, feed.img3, feed.img4, feed.img5,
                        feed.tag1, feed.tag2, feed.tag3, feed.tag4, feed.tag5,
                        feed.isDeleted,
                        user.userId.as("writerUserId"),
                        user.nickName.as("writerId"),
                        user.profile.as("writerProfile"),
                        user.userBadgeId.as("writerBadge"),
                        comment.countDistinct().as("commentsCount"),
                        likeList.countDistinct().as("likesCount")
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(comment).on(comment.feed.feedId.eq(feed.feedId))
                .leftJoin(likeList).on(likeList.feed.feedId.eq(feed.feedId))
                .where(feed.isDeleted.eq(false))
                .groupBy(feed.feedId)
                .orderBy(getSortOrder(sortType, comment, likeList, feed))
                .fetch();
    }

    /**
     * 정렬 기준에 따라 OrderSpecifier를 반환
     */
    private OrderSpecifier<?> getSortOrder(String sortType, QComment comment, QLikeList likeList, QFeed feed) {
        switch (sortType) {
            case "likes":
                return likeList.countDistinct().desc();
            case "comments":
                return comment.countDistinct().desc();
            case "follow":
            case "all":
            default:
                return feed.createDate.desc();
        }
    }
}