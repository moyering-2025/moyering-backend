package com.dev.moyering.common.repository;

import static com.dev.moyering.admin.entity.QAdminNotice.adminNotice;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor 
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
	@Override
    public long countVisibleNotices() throws Exception {
        return queryFactory
                .select(adminNotice.count())
                .from(adminNotice)
                .where(
                        adminNotice.isHidden.isFalse()
                )
                .fetchOne();
    }
	@Override
    public List<AdminNoticeDto> findNoticesByPage(PageRequest pageRequest) throws Exception {
        // 데이터 조회
        List<AdminNoticeDto> noticeList = queryFactory
                .select(Projections.constructor(AdminNoticeDto.class,
                        adminNotice.noticeId,
                        adminNotice.title,
                        adminNotice.content,
                        adminNotice.pinYn,
                        adminNotice.isHidden,
                        adminNotice.createdAt
                ))
                .from(adminNotice)
                .where(
                        adminNotice.isHidden.isFalse()
                )
                .orderBy(
                        adminNotice.pinYn.desc(), 
                        adminNotice.noticeId.desc() // 그 다음 최신 작성일 순 (내림차순)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        return noticeList;
    }
}
