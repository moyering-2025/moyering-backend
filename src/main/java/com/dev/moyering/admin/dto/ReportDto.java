package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.Report;
import com.dev.moyering.admin.entity.ReportProcessStatus;
import com.dev.moyering.admin.entity.ReportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder


public class ReportDto {
    private Integer reportId; // 신고 id
    private String reporterId; // 신고자 id
    private String targetOwnerId; // 피신고자 id
    private ReportType reportType; // 신고 유형 (게시글 : PT, 댓글 : CT, 사용자 : UR, 강의 : CL)
    private String targetId; // 게시글 id, 사용자 Id, 강의 id,,
    private String title; // 제목
    private String content; // 내용 (신고사유)
    private LocalDateTime createdAt; // 등록일시
    private LocalDateTime updatedDate; // 업데이트 일시
    private String processorId;  // 처리자 아이디
    private LocalDateTime processedDate; // 처리완료시간
    private ReportProcessStatus processStatus; // 처리상태 (대기, 숨기기, 보이기) - 숨기기 : 처리, 보이;기 : 미처리

    // DTO -> entity 변환
    public Report toEntity() {
        return Report.builder()
                .reporterId(this.reporterId)
                .targetOwnerId(this.targetOwnerId)
                .reportType(this.reportType)
                .targetId(this.targetId)
                .title(this.title)
                .content(this.content)
//                .processorId(this.processorId)
//                .processedDate(this.processedDate)
//                .processStatus(this.processStatus)
                .build();
    }
}