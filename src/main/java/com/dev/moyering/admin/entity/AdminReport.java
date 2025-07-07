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
@Table (name = "report")
public class AdminReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId; // 신고 id

    @Column(name = "reporter_id", nullable = false)
    private String reporterId; // 신고자 id

    @Column(name = "reported_id", nullable = false)
    private String reported_id; // 피신고자 id

    // 타겟 ID (신고자 ,댓글 NO, 코멘트 NO,..)
    @Column(name = "target_id", nullable = false)
    private String targetId;


    // 실제 데이터 크기만큼 저장
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Lob
    private String content; // 내용

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; // 등록일시


    @Enumerated(EnumType.STRING)
    @Column(name = "process_status", nullable = false)
    private ReportProcessStatus processStatus; // 처리상태 (대기, 숨기기, 보이기) - 숨기기 : 처리, 보이기 : 미처리


    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType; // 신고 유형 (게시글 : PT, 댓글 : CT, 사용자 : UR, 강의 : CL)

    @Builder
    public AdminReport(Integer reportId, String reporterId, String reported_id, String targetId, String content, LocalDateTime createdAt, ReportProcessStatus processStatus, ReportType reportType) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reported_id = reported_id;
        this.targetId = targetId;
        this.content = content;
        this.createdAt = createdAt;
        this.processStatus = processStatus;
        this.reportType = reportType;
    }

    // 생성에 필요한 최소한의 정보

    // 엔티티 -> toDto
    public AdminReportDto toDto() {
        return AdminReportDto.builder()
                .reportId(this.reportId)
                .reporterId(this.reporterId)
                .reportType(this.reportType)
                .targetId(this.targetId)
                .content(this.content)
                .createdAt(this.createdAt)
                .processStatus(this.processStatus)
                .reportType(this.reportType)
                .build();
    }

    /* 비즈니스 메서드 */
    // 신고처리 로직
    public void process(String processorId) {
        this.processStatus = ReportProcessStatus.RESOLVED;
    }

    // 숨기기 로직
    public void hide(String processorId) {
        this.processStatus = ReportProcessStatus.HIDDEN;

    }

    // PENDING -> 처리 로직
    public boolean isProcessed() {
        return processStatus != ReportProcessStatus.PENDING;
    }
}