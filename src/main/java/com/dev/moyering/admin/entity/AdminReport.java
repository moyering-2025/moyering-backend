package com.dev.moyering.admin.entity;

import com.dev.moyering.admin.dto.AdminReportDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

//신고 정보: 신고자, 피신고자, 제목, 내용
//신고 유형: 게시글(PT), 댓글(CT), 사용자(UR), 강의(CL)
//처리 상태: PENDING(대기) → RESOLVED(해결) / HIDDEN(숨기기)
//감사 정보: 생성/수정일시, 처리자/처리일시
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AdminReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId; // 신고 id

    @Column(name = "reporter_id", nullable = false)
    private String reporterId; // 신고자 id

    @Column(name = "target_owner_id", nullable = false)
    private String targetOwnerId; // 피신고자 id

    // 타겟 ID (신고자 ,댓글 NO, 코멘트 NO,..)
    @Column(name = "target_id", nullable = false)
    private String targetId;

    @Column(nullable = false)
    private String title; // 제목

    // 실제 데이터 크기만큼 저장
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Lob
    private String content; // 내용

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; // 등록일시

    @Column(name = "processor_id")
    private String processorId; // 처리자 아이디

    @Column(name = "processed_date")
    private LocalDateTime processedDate; // 처리완료시간

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status", nullable = false)
    private ReportProcessStatus processStatus; // 처리상태 (대기, 숨기기, 보이기) - 숨기기 : 처리, 보이기 : 미처리

    @Column(name = "is_hidden", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isHidden = false; // 초기에는 보이도록 설정

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType; // 신고 유형 (게시글 : PT, 댓글 : CT, 사용자 : UR, 강의 : CL)

    // 생성에 필요한 최소한의 정보
    @Builder
    private AdminReport(String reporterId, String targetId, String targetOwnerId,
                        String title, String content, ReportType reportType) {
        this.reporterId = reporterId;
        this.targetOwnerId = targetOwnerId;
        this.targetId = targetId;
        this.title = title;
        this.content = content;
        this.reportType = reportType;
        this.processStatus = ReportProcessStatus.PENDING; //대기 중
        this.isHidden = false;
    }

    // 엔티티 -> toDto
    public AdminReportDto toDto() {
        return AdminReportDto.builder()
                .reportId(this.reportId)
                .reporterId(this.reporterId)
                .reportType(this.reportType)
                .targetId(this.targetId)
                .targetOwnerId(this.targetOwnerId)
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .processorId(this.processorId)
                .processedDate(this.processedDate)
                .processStatus(this.processStatus)
                .isHidden(this.isHidden)
                .build();
    }

    /* 비즈니스 메서드 */
    // 신고처리 로직
    public void process(String processorId) {
        this.processStatus = ReportProcessStatus.RESOLVED;
        this.processedDate = LocalDateTime.now();
        this.processorId = processorId;
    }

    // 숨기기 로직
    public void hide(String processorId) {
        this.processStatus = ReportProcessStatus.HIDDEN;
        this.processedDate = LocalDateTime.now();
        this.processorId = processorId;
        this.isHidden = true;
    }

    // PENDING -> 처리 로직
    public boolean isProcessed() {
        return processStatus != ReportProcessStatus.PENDING;
    }
}