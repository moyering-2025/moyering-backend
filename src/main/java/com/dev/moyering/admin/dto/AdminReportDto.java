package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminReport;
import com.dev.moyering.admin.entity.ReportProcessStatus;
import com.dev.moyering.admin.entity.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminReportDto {
    private Integer reportId;
    private String reporterId; // 신고자 id
    private String reportedId; // 피신고자 id
    private ReportType reportType; // 신고 유형 (게시글 : PT, 댓글 : CT, 사용자 : UR, 강의 : CL)
    private String targetId; // 게시글 id, 사용자 Id, 강의 id,,
    private String content; // 내용 (신고사유)
    private LocalDateTime createdAt; // 등록일시
    private ReportProcessStatus processStatus; // 처리상태 (대기, 숨기기, 보이기) - 숨기기 : 처리, 보이;기 : 미처리

    @Builder
    public AdminReportDto(Integer reportId, String reporterId, String reportedId, ReportType reportType, String targetId, String content, LocalDateTime createdAt, ReportProcessStatus processStatus) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.reportType = reportType;
        this.targetId = targetId;
        this.content = content;
        this.createdAt = createdAt;
        this.processStatus = processStatus;
    }

    // DTO -> entity 변환
    public AdminReport toEntity() {
        return AdminReport.builder()
                .reportId(this.reportId)
                .reporterId(this.reporterId)
                .reported_id(this.reportedId)
                .reportType(this.reportType)
                .targetId(this.targetId)
                .content(this.content)
                .createdAt(this.createdAt)
                .processStatus(this.processStatus)
                .build();
    }
}