package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminReportDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import static com.dev.moyering.admin.entity.QAdminReport.adminReport;


import java.util.List;



@Repository
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class AdminReportRepositoryImpl implements AdminReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminReportDto> findReportsByKeyword(String searchKeyword, Pageable pageable) {

        // 데이터 조회
        List<AdminReportDto> content = queryFactory
                .select(Projections.constructor(AdminReportDto.class,
                        // ReportDto의 생성자에 들어갈 컬럼 순서
                        adminReport.reportId,
                        adminReport.reporterId,
                        adminReport.reportType,
                        adminReport.targetId,
                        adminReport.content,
                        adminReport.createdAt,
                        adminReport.processStatus
                        ))
                        .from(adminReport)
                        .where(
                                searchReport(searchKeyword) // 검색 기능 (검색 조건 메서드 호출)
                        )
                        .orderBy(
                                adminReport.createdAt.desc()
//                        adminReport.processYn.desc()
                        )
                        .offset(pageable.getOffset()) // 몇 번째 데이터부터 가져올지 (페이지 * 크기)
                        .limit(pageable.getPageSize()) // 몇 개를 가져올지 (한 페이지 크기)
                        .fetch(); // 쿼리 실행하고 결과를 List로 받아오기

                // 전체 데이터 개수 조회 (페이징 위함)
                Long total = queryFactory
                        .select(adminReport.count()) // COUNT(*) 쿼리: 전체 개수만 세기
                        .from(adminReport)
                        .where(
                                searchReport(searchKeyword) // 위 검색과 같은 검색 조건 적용
                        )
                        .fetchOne(); // 쿼리 실행하고 결과를 Long으로 받아오기

//        Spring의 Page 객체로 변환해서 반환
                return new PageImpl<>(content, pageable, total); // 실제 데이터 리스트, 페이징 정보(현재 페이지, 크기), 전체 데이터 개수 가져오기
            }

            // 신고관리 제목, 내용 검색
            private BooleanExpression searchReport(String keyword) {
                if (keyword == null || keyword.trim().isEmpty()) {
                    return null; //검색 없으면 전체 조회
                }
                // 검색어가 있으면 제목 또는 내용에 포함된 데이터 찾기
                 return adminReport.content.containsIgnoreCase(keyword); // 또는 내용에 검색어 포함
            }
        }
