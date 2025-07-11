package com.dev.moyering.classring.dto;

import java.sql.Date;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentHistoryDto {
    private Integer paymentId;
    private String classTitle; // 클래스명
    private String status;     // 모집중, 운영종료 등
    private Date startDate; //수업일
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime scheduleStart;  // 시간 (예: 오전 9시 ~ 오후 12시)
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime scheduleEnd;  // 시간 (예: 오전 9시 ~ 오후 12시)
    private String locName;    // 장소명
    private String addr;       // 주소
    private Integer amount;    // 결제 금액
    private String imageUrl;   // 대표 이미지
    private String paymentStatus; // 결제 상태
    private String itemsName; //강의자료
    private Integer calendarId;
    private Integer min;
    private Integer max;
    private Integer regCnt;
}
