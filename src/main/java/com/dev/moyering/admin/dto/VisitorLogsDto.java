package com.dev.moyering.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorLogsDto {
    private LocalDate visitDate;
    private Long visitorCount;
    private Long memberCount;    // 회원 방문자 수
    private Long guestCount;     // 비회원 방문자 수
}
