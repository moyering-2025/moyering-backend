package com.dev.moyering.gathering.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;

@Service
public class GatheringSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(GatheringSchedulerService.class);

    @Autowired
    private GatheringRepository gatheringRepository;

    @Autowired
    private GatheringApplyRepository gatheringApplyRepository;

    @Autowired
    private AlarmService alarmService;

    /**
     * 매 30분마다 실행되는 스케줄러
     * 신청 마감일이 지난 모임들을 확인하여 최소 참여 인원 미달 시 취소 처리
     */
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    @Transactional
    public void checkAndCancelGatheringsByDeadline() {
        try {
            logger.info("모임 신청 마감일 확인 및 취소 처리 스케줄러 시작");
            // 현재 시간
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            // 신청 마감일이 지났고, 아직 취소되지 않은 모임 목록 조회
            List<Gathering> expiredGatherings = gatheringRepository.findExpiredGatherings(currentTime);
            
            logger.info("신청 마감일이 지난 모임 수: {}", expiredGatherings.size());
            
            for (Gathering gathering : expiredGatherings) {
                processExpiredGathering(gathering);
            }
            
            logger.info("모임 신청 마감일 확인 및 취소 처리 스케줄러 완료");
            
        } catch (Exception e) {
            logger.error("모임 신청 마감일 확인 중 오류 발생", e);
        }
    }

    /**
     * 매일 오전 9시에 실행되는 스케줄러 (보조 스케줄러)
     * 하루에 한 번 전체적으로 확인
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void dailyGatheringCheck() {
        try {
            logger.info("일일 모임 확인 스케줄러 시작");
            checkAndCancelGatheringsByDeadline();
            logger.info("일일 모임 확인 스케줄러 완료");
        } catch (Exception e) {
            logger.error("일일 모임 확인 중 오류 발생", e);
        }
    }

    /**
     * 개별 모임 처리 메서드
     */
    private void processExpiredGathering(Gathering gathering) {
        try {
            Integer gatheringId = gathering.getGatheringId();
            Integer minAttendees = gathering.getMinAttendees();
            
            // 승인된 참여자 수 조회
            Integer approvedCount = gatheringApplyRepository.countByGatheringGatheringIdAndIsApprovedTrue(gatheringId).intValue();            
            logger.info("모임 ID: {}, 최소 참여 인원: {}, 승인된 참여자 수: {}", 
                       gatheringId, minAttendees, approvedCount);
            
            // 승인된 참여자 수가 최소 참여 인원보다 적으면 취소 처리
            if (approvedCount == null || approvedCount < minAttendees) {
                // 모임 취소 처리
                gatheringRepository.updateGatheringStatus(gatheringId, true);
                
                logger.info("모임 ID: {} 최소 참여 인원 미달로 취소 처리됨", gatheringId);
                
                // 모임 주최자에게 알림 전송
                sendCancellationAlarmToOrganizer(gathering, approvedCount, minAttendees);
                
                // 승인된 참여자들에게 알림 전송
                sendCancellationAlarmToParticipants(gathering);
                
            } else {
                logger.info("모임 ID: {} 최소 참여 인원 충족으로 정상 진행", gatheringId);
            }
            
        } catch (Exception e) {
            logger.error("모임 ID: {} 처리 중 오류 발생", gathering.getGatheringId(), e);
        }
    }

    private void sendCancellationAlarmToOrganizer(Gathering gathering, Integer approvedCount, Integer minAttendees) {
        try {
            AlarmDto alarmDto = AlarmDto.builder()
                    .alarmType(3) // 게더링 알람
                    .title("모임 취소")
                    .receiverId(gathering.getUser().getUserId())
                    .senderId(null) // 시스템 알림
                    .senderNickname("시스템")
                    .content(String.format("'%s' 모임이 최소 참여 인원 미달로 자동 취소되었습니다.", 
                            gathering.getTitle()))
                    .build();
            
            alarmService.sendAlarm(alarmDto);
            logger.info("모임 주최자에게 취소 알림 전송 완료 - 모임 ID: {}", gathering.getGatheringId());
            
        } catch (Exception e) {
            logger.error("모임 주최자 취소 알림 전송 실패 - 모임 ID: {}", gathering.getGatheringId(), e);
        }
    }

    /**
     * 승인된 참여자들에게 취소 알림 전송
     */
    private void sendCancellationAlarmToParticipants(Gathering gathering) {
        try {
            // 기존 메서드를 활용하여 승인된 참여자 목록 조회
            List<GatheringApplyDto> approvedUsers = gatheringApplyRepository.findApprovedUserListByGatheringId(gathering.getGatheringId());
            
            for (GatheringApplyDto approvedUser : approvedUsers) {
                AlarmDto alarmDto = AlarmDto.builder()
                        .alarmType(3) // 게더링 알람
                        .title("참여 모임 취소")
                        .receiverId(approvedUser.getUserId())
                        .senderId(null) // 시스템 알림
                        .senderNickname("시스템")
                        .content(String.format("참여하신 '%s' 모임이 최소 참여 인원 미달로 취소되었습니다.", 
                                gathering.getTitle()))
                        .build();
                
                alarmService.sendAlarm(alarmDto);
            }
            
            logger.info("승인된 참여자들에게 취소 알림 전송 완료 - 모임 ID: {}, 참여자 수: {}", 
                       gathering.getGatheringId(), approvedUsers.size());
            
        } catch (Exception e) {
            logger.error("참여자 취소 알림 전송 실패 - 모임 ID: {}", gathering.getGatheringId(), e);
        }
    }
}