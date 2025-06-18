package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.ReportDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dev.moyering.admin.entity.QReport.report;

@Repository
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportDto> findReportsByKeyword(String searchKeyword, Pageable pageable) {

        // 데이터 조회
        List<ReportDto> content = queryFactory
                .select(Projections.constructor(ReportDto.class,
                        // ReportDto의 생성자에 들어갈 컬럼 순서
                        report.reportId,
                        report.reporterId,
                        report.targetOwnerId,
                        report.reportType,
                        report.targetId,
                        report.title,
                        report.content,
                        report.createdAt,
                        report.processorId,
                        report.processedDate,
                        report.processStatus
                        ))
                        .from(report)
                        .where(
                                searchReport(searchKeyword) // 검색 기능 (검색 조건 메서드 호출)
                        )
                        .orderBy(
                                report.createdAt.desc()
//                        report.processYn.desc()
                        )
                        .offset(pageable.getOffset()) // 몇 번째 데이터부터 가져올지 (페이지 * 크기)
                        .limit(pageable.getPageSize()) // 몇 개를 가져올지 (한 페이지 크기)
                        .fetch(); // 쿼리 실행하고 결과를 List로 받아오기

                // 전체 데이터 개수 조회 (페이징 위함)
                Long total = queryFactory
                        .select(report.count()) // COUNT(*) 쿼리: 전체 개수만 세기
                        .from(report)
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
                return report.title.containsIgnoreCase(keyword)  // 제목에 검색어 포함 (대소문자 무시)
                        .or(report.content.containsIgnoreCase(keyword)); // 또는 내용에 검색어 포함
            }
        }
