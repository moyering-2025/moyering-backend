package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.QScrapListDto;
import com.dev.moyering.socialing.dto.ScrapListDto;
import com.dev.moyering.socialing.entity.QFeed;
import com.dev.moyering.socialing.entity.QScrap;
import com.dev.moyering.socialing.entity.Scrap;
import com.dev.moyering.user.entity.QUser;
import com.dev.moyering.user.entity.QUserBadge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScrapRepositoryImpl implements ScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QScrap scrap = QScrap.scrap;

    @Override
    public Optional<Scrap> findScrapByUserIdAndFeedId(Integer userId, Integer feedId) {
        Scrap result = queryFactory
                .selectFrom(scrap)
                .where(scrap.user.userId.eq(userId)
                        .and(scrap.feed.feedId.eq(feedId)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<Integer> findFeedIdsByUserId(Integer userId) {
        return queryFactory
                .select(scrap.feed.feedId)
                .from(scrap)
                .where(scrap.user.userId.eq(userId))
                .fetch();
    }

    @Override
    public void deleteByUserIdAndFeedId(Integer userId, Integer feedId) {
        queryFactory
                .delete(scrap)
                .where(scrap.user.userId.eq(userId)
                        .and(scrap.feed.feedId.eq(feedId)))
                .execute();
    }

    @Override
    public List<ScrapListDto> findMyScrapsCursor(Integer userId, Integer lastScrapId, int size) {
        QScrap scrap = QScrap.scrap;
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QUserBadge userBadge = QUserBadge.userBadge;

        return queryFactory
                .select(new QScrapListDto(
                        scrap.scrapId,
                        feed.feedId,
                        feed.content,
                        feed.img1,
                        feed.createDate,
                        user.userId,
                        user.nickName,
                        user.profile,
                        userBadge.badge_img
                ))
                .from(scrap)
                .join(scrap.feed, feed)
                .join(feed.user, user)
                .leftJoin(userBadge)
                .on(userBadge.user.userId.eq(user.userId)
                        .and(userBadge.isRepresentative.isTrue()))
                .where(
                        scrap.user.userId.eq(userId),
                        lastScrapId != null ? scrap.scrapId.lt(lastScrapId) : null
                )
                .orderBy(scrap.scrapId.desc())
                .limit(size)
                .fetch();
    }
}

