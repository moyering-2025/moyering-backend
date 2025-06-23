package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.BannerDto;

import com.dev.moyering.admin.entity.Banner;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.dev.moyering.admin.entity.QBanner.banner;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BannerDto> findBannerByKeyword(String keyword, Pageable pageable) {
        List<BannerDto> content = queryFactory
                .select(Projections.constructor(BannerDto.class,
                        banner.bannerId, // 배너아이디;
                        banner.bannerImg, // 배너이미지
                        banner.createdAt, // 등록일자
                        banner.title, // 제목
                        banner.content, // 내용
                        banner.status, // 배너 상태 (숨기기, 보이기)
                        banner.user.userId // 등록자 (관리자)
                ))
                .from(banner)
                .where(
                        searchBanner(keyword)
                )
                .orderBy(
                        banner.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 반환값 : Page
        return new PageImpl<>(content, pageable, content.size());
    }


    private BooleanExpression searchBanner(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null; // 검색 없으면 전체 조회
        }
        // 검색어가 있으면 제목 또는 내용에 포함된 데이터 찾기
        return banner.title.containsIgnoreCase(keyword);
//                .or(banner.content.contains(keyword));
    }

    @Override // 단건 조회
    public BannerDto findBannerByBannerId(Integer bannerId) {
        BannerDto result = queryFactory
                .select(Projections.constructor(BannerDto.class,
                        banner.bannerId,
                        banner.bannerImg,
                        banner.createdAt,
                        banner.title,
                        banner.content,
                        banner.status,
                        banner.user.userId
                ))
                .from(banner)
                .where(banner.bannerId.eq(bannerId))
                .fetchOne();

        if (result == null) {
            throw new EntityNotFoundException("Banner not found with id: " + bannerId);
        }

        return result;
    }
}