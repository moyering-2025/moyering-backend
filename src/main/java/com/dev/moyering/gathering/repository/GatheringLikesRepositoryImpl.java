package com.dev.moyering.gathering.repository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringLikes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.querydsl.core.types.dsl.Expressions.constant;  // 추가

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringLikesRepositoryImpl implements GatheringLikesRepositoryCustom {
	
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public Integer countByGatheringId(Integer gatheringId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		Long res = jpaQueryFactory.select(gatheringLikes.count())
					.where(gatheringLikes.gathering.gatheringId.eq(gatheringId))
					.from(gatheringLikes)
					.fetchOne();
		
		return res.intValue();
	}
	@Override
	public Integer selectGatheringLikes(Integer userId, Integer gatheringId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		return jpaQueryFactory.select(gatheringLikes.gatheringLikeId)
				.from(gatheringLikes)
				.where(gatheringLikes.gathering.gatheringId.eq(gatheringId)
						.and(gatheringLikes.user.userId.eq(userId))
						)
				.fetchOne();
	}
	@Override
	@Transactional
	public void deleteGatheringLikes(Integer gatheringLikeId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		
		jpaQueryFactory.delete(gatheringLikes)
			.where(gatheringLikes.gatheringLikeId.eq(gatheringLikeId)).execute();
	}
	@Override
	public Page<WishlistItemDto> searchByUser(UtilSearchDto dto, Pageable pageable) throws Exception {
	    QGatheringLikes gl = QGatheringLikes.gatheringLikes;
	    QGathering g = QGathering.gathering;
        BooleanBuilder builder = new BooleanBuilder();
        
        builder.and(gl.user.userId.eq(dto.getUserId()));
        if (dto.getKeywords() != null && !dto.getKeywords().isBlank()) {
            builder.and(g.title.containsIgnoreCase(dto.getKeywords()));
        }
        // 2) 데이터 쿼리
        var content = jpaQueryFactory
            .select(Projections.constructor(
                WishlistItemDto.class,
                gl.gatheringLikeId,
                constant("gathering"),
                g.title,
                g.meetingDate,
                g.thumbnail,
                g.startTime,
                g.endTime,
                gl.user.userId,
                g.address,
                g.detailAddress,
                g.locName,
                g.gatheringId
            ))
            .from(gl)
            .join(gl.gathering, g)
            .where(builder)
            .orderBy(gl.gatheringLikeId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 3) 전체 개수 카운트
        long total = jpaQueryFactory
            .select(gl.count())
            .from(gl)
            .join(gl.gathering, g)
            .where(builder)
            .fetchOne();

        // 4) PageImpl 로 래핑하여 반환
        return new PageImpl<>(content, pageable, total);
	}


}
