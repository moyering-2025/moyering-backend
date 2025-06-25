package com.dev.moyering.admin.repository;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.dev.moyering.admin.entity.QVisitorLogs;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminVisitorLogsRepositoryImpl implements AdminVisitorLogsRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QVisitorLogs visitorLogs = QVisitorLogs.visitorLogs;

    @Override
    public boolean existsBySessionIdAndVisitDate(String sessionId, LocalDate visitDate) {
        Integer exists = queryFactory
                .selectOne()
                .from(visitorLogs)
                .where(
                        visitorLogs.sessionId.eq(sessionId)
                                .and(visitorLogs.visitDate.eq(visitDate))
                )
                .fetchFirst();

        return exists != null;
    }

    @Override
    public long countByVisitDate(LocalDate visitDate) {
        Long count = queryFactory
                .select(visitorLogs.count())
                .from(visitorLogs)
                .where(visitorLogs.visitDate.eq(visitDate))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public long countByVisitDateBetween(LocalDate startDate, LocalDate endDate) {
        Long count = queryFactory
                .select(visitorLogs.count())
                .from(visitorLogs)
                .where(visitorLogs.visitDate.between(startDate, endDate))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public long countByVisitDateAndMemberYn(LocalDate date, Boolean isMember) {
        Long count = queryFactory
                .select(visitorLogs.count())
                .from(visitorLogs)
                .where(
                        visitorLogs.visitDate.eq(date)
                                .and(visitorLogs.memberYn.eq(isMember))
                )
                .fetchOne();

        return count != null ? count : 0L;
    }
}