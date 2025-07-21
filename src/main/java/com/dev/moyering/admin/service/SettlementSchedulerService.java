package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.SettlementProcessDto;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.dev.moyering.admin.entity.QAdminSettlement.adminSettlement;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QHost.host;
import static com.dev.moyering.user.entity.QUserPayment.userPayment;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettlementSchedulerService {
    private final JPAQueryFactory queryFactory;
    private final AdminSettlementRepository adminSettlementRepository;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void processAutoSettlement() {
        log.info("=== 정산 자동 처리 스케줄러 시작 ===");
        Date oneDaysAgo = Date.valueOf(LocalDate.now().minusDays(1));

        // 7일 전 종료된 클래스 중 정산 미처리된 것들 조회
        List<SettlementProcessDto> targetClasses = queryFactory
                .select(Projections.constructor(SettlementProcessDto.class,
                        classCalendar.calendarId,
                        host.hostId,
                        host.bankName,
                        host.accNum,
                        userPayment.amount.sum()
                                .subtract(userPayment.platformFee.sum()).coalesce(0) // 정산 예정 금액
                ))
                .from(classCalendar)
                .join(host).on(host.hostId.eq(classCalendar.hostClass.host.hostId))
                .leftJoin(userPayment).on(
                        userPayment.classCalendar.calendarId.eq(classCalendar.calendarId)
                                .and(userPayment.status.eq("결제완료"))
                )
                .leftJoin(adminSettlement).on(
                        adminSettlement.classCalendar.calendarId.eq(classCalendar.calendarId)
                )
                .where(
                        classCalendar.endDate.eq(oneDaysAgo) // 7일 전 종료
                                .and(classCalendar.status.eq("종료")) // 클래스 완료 상태
                                .and(adminSettlement.settlementId.isNull()) // 정산 테이블에 없는 것만
                )
                .groupBy(
                        classCalendar.calendarId,
                        host.hostId,
                        host.bankName,
                        host.accNum
                )
                .fetch();

        if (targetClasses.isEmpty()) {
            log.info("정산 처리 대상 클래스가 없습니다.");
            return;
        }

        log.info("정산 처리 대상: {}개 클래스", targetClasses.size());

        // 정산 데이터 생성 및 저장
        int successCount = 0;
        int failCount = 0;

        for (SettlementProcessDto dto : targetClasses) {
            try {
                // 정산 예정일 = 클래스 종료일 + 1일
                LocalDate settlementDate = oneDaysAgo.toLocalDate().plusDays(1);

                AdminSettlement settlement = AdminSettlement.builder()
                        .classCalendar(getClassCalendarById(dto.getCalendarId()))
                        .hostId(dto.getHostId())
                        .settlementDate(settlementDate)
                        .settlementStatus("WT") // 대기 상태
                        .bankName(dto.getBankName())
                        .accNum(dto.getAccNum())
                        .settleAmountToDo(dto.getSettleAmountToDo())
                        .settlementAmount(null) // 실제 지급액은 나중에 설정
                        .build();

                adminSettlementRepository.save(settlement);

                successCount++;


            } catch (Exception e) {
                failCount++;
                log.error("클래스 캘린더 ID: {} 정산 처리 중 오류 발생", dto.getCalendarId(), e);
            }
        }

        log.info("=== 정산 자동 처리 완료 - 성공: {}개, 실패: {}개 ===", successCount, failCount);
    }

    // ClassCalendar 조회 헬퍼 메서드
    private com.dev.moyering.host.entity.ClassCalendar getClassCalendarById(Integer calendarId) {
        return queryFactory
                .selectFrom(classCalendar)
                .where(classCalendar.calendarId.eq(calendarId))
                .fetchOne();
    }
}
