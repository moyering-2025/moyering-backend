package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.QScrap;
import com.dev.moyering.socialing.entity.Scrap;
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
}
