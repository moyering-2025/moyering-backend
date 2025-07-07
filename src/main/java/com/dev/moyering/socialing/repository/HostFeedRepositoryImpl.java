package com.dev.moyering.socialing.repository;

import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.socialing.dto.HostFeedDto;
import com.dev.moyering.socialing.entity.QHostFeed;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.bean;

@Repository
@RequiredArgsConstructor
public class HostFeedRepositoryImpl implements HostFeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HostFeedDto> findHostFeeds(Integer hostId, int offset, int size) {
        QHostFeed feed = QHostFeed.hostFeed;
        QHost host = QHost.host;

        return queryFactory
                .select(bean(HostFeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1,
                        feed.img2,
                        feed.img3,
                        feed.img4,
                        feed.img5,
                        feed.createDate,
                        feed.updateDate,
                        feed.isDeleted,
                        feed.tag1,
                        feed.tag2,
                        feed.tag3,
                        feed.tag4,
                        feed.tag5,
                        feed.category,
                        host.hostId.as("hostId"),
                        host.name.as("hostName"),
                        host.profile.as("hostProfile")
                ))
                .from(feed)
                .join(feed.host, host)
                .where(feed.host.hostId.eq(hostId))
                .offset(offset)
                .limit(size)
                .orderBy(feed.createDate.desc())
                .fetch();
    }

    @Override
    public List<HostFeedDto> findHostFeedsByCategory(Integer hostId, String category, int offset, int size) {
        QHostFeed feed = QHostFeed.hostFeed;
        QHost host = QHost.host;

        return queryFactory
                .select(Projections.bean(HostFeedDto.class,
                        feed.feedId,
                        feed.content,
                        feed.img1,
                        feed.img2,
                        feed.img3,
                        feed.img4,
                        feed.img5,
                        feed.createDate,
                        feed.updateDate,
                        feed.isDeleted,
                        feed.tag1,
                        feed.tag2,
                        feed.tag3,
                        feed.tag4,
                        feed.tag5,
                        feed.category,
                        host.hostId.as("hostId"),
                        host.name.as("hostName"),
                        host.profile.as("hostProfile")
                ))
                .from(feed)
                .join(feed.host, host)
                .where(
                        feed.host.hostId.eq(hostId),
                        feed.category.eq(category)
                )
                .offset(offset)
                .limit(size)
                .orderBy(feed.createDate.desc())
                .fetch();
    }
}
