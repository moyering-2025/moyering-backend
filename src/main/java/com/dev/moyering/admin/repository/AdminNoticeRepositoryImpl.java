package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.entity.AdminNotice;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.dev.moyering.admin.entity.QAdminNotice.adminNotice;

@Repository
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class AdminNoticeRepositoryImpl implements AdminNoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminNoticeDto> findNoticesByKeyword(String searchKeyword, Pageable pageable) {

        // 데이터 조회
        List<AdminNoticeDto> content = queryFactory
                .select(Projections.constructor(AdminNoticeDto.class,
                        // AdminNoticeDto의 생성자에 들어갈 컬럼 순서
                        adminNotice.noticeId,
                        adminNotice.title,
                        adminNotice.content,
                        adminNotice.pinYn,
                        adminNotice.isHidden,
                        adminNotice.createdAt
                ))
                .from(adminNotice)
                .where(
                        searchNotice(searchKeyword) // 검색 기능 (검색 조건 메서드 호출)
                )
                .orderBy(
                        adminNotice.pinYn.desc(), // 고정글(true) 먼저 (내림차순)
                        adminNotice.createdAt.desc() // 그 다음 최신 작성일 순 (내림차순)
                )

                .offset(pageable.getOffset()) // 몇 번째 데이터부터 가져올지 (페이지 * 크기)
                .limit(pageable.getPageSize()) // 몇 개를 가져올지 (한 페이지 크기)
                .fetch(); // 쿼리 실행하고 결과를 List로 받아오기

        // 전체 데이터 개수 조회 (페이징 위함)
        Long total = queryFactory
                .select(adminNotice.count()) // COUNT(*) 쿼리: 전체 개수만 세기
                .from(adminNotice)
                .where(
                        searchNotice(searchKeyword) // 위 검색과 같은 검색 조건 적용
                )
                .fetchOne(); // 쿼리 실행하고 결과  받아오기

//        Spring의 Page 객체로 변환해서 반환
        return new PageImpl<>(content, pageable, total != null ? total : 0); // 실제 데이터 리스트, 페이징 정보(현재 페이지, 크기), 전체 데이터 개수 가져오기
    }

    @Override
    public AdminNoticeDto findNoticeByNoticeId(Integer noticeId) {
        return queryFactory
                .select(Projections.constructor(AdminNoticeDto.class,
                        adminNotice.noticeId,
                        adminNotice.title,
                        adminNotice.content,
                        adminNotice.pinYn,
                        adminNotice.isHidden,
                        adminNotice.createdAt,
                        adminNotice.lastModifiedDate
                        ))
                .from(adminNotice)
                .where(
                        adminNotice.noticeId.eq(noticeId)
                )
                .fetchOne(); //단건조회
    }

    // 공지사항 제목, 내용 검색
    private BooleanExpression searchNotice(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null; //검색 없으면 전체 조회
        }
        // 검색어가 있으면 제목 또는 내용에 포함된 데이터 찾기
        return adminNotice.title.containsIgnoreCase(keyword)  // 제목에 검색어 포함 (대소문자 무시)
                .or(adminNotice.content.containsIgnoreCase(keyword)); // 또는 내용에 검색어 포함
    }
    // 삭제는 리파지토리에서 구현 X => 서비스에서 notice.hide() => 화면에서만 삭제하기
}
