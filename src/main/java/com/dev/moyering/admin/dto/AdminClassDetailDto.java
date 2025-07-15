package com.dev.moyering.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminClassDetailDto {
    // 기본 클래스 정보
    private Integer classId;
    private Integer calendarId;
    private String className;
    private String hostName;
    private String processStatus;
    private Integer currentCount;
    private Integer recruitMax;
    private Integer recruitMin;

    // 카테고리 정보
    private String firstCategory;
    private String secondCategory;

    // 가격 및 일정
    private Integer price;
    private Date startDate;
    private Date endDate;
    private LocalTime scheduleStart;
    private LocalTime scheduleEnd;

    private List<ScheduleDto> schedules;

    // 위치 정보
    private String location;
    private String detailAddr;

    // 상세 정보
    private String description;
    private String keywords;
    private String inclusion;
    private String preparation;
    private String caution;

    // 파일 정보
    private String portfolioName;
    private String materialName;

    // 이미지 정보
    private String imgName1;
    private String imgName2;
    private String imgName3;
    private String imgName4;
    private String imgName5;

    // 수강생 목록
    private List<StudentDto> students;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentDto {
        private String userId;
        private String username;
        private String name;
        private String phone;
        private String email;
        private Date regDate;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDto {
        private Integer calendarId;     // 일정 ID
        private Date startDate;         // 시작일 (기존 Date 타입 유지)
        private Date endDate;           // 종료일
        private String status;          // 상태 (승인대기, 모집중, 종료, 취소 등)
        private Integer registeredCount; // 현재 등록된 수강생 수
    }
}