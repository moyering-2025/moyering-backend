package com.dev.moyering.admin.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "visitor_logs")
public class VisitorLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitorId;

    @Column
    private Integer userId; // NULL이면 비회원

    @Column(nullable = false)
    private String sessionId; // 중복 방문 체크용

    @Column(nullable = false)
    private String ipAddress; // IP 주소

    @Column(nullable = false)
    private LocalDate visitDate; // 날짜만 저장

    @Column(nullable = false)
    private LocalDateTime visitTime; // 정확한 방문 시간

    @Column(nullable = false)
    private Boolean memberYn; // 회원 여부

    @Builder
    public VisitorLogs(Integer userId, String sessionId, String ipAddress,
                       LocalDate visitDate, LocalDateTime visitTime, Boolean memberYn) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.ipAddress = ipAddress;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.memberYn = memberYn;
    }

    // 생성 시 자동 설정
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.visitTime == null) {
            this.visitTime = now;
        }
        if (this.visitDate == null) {
            this.visitDate = now.toLocalDate();
        }
        if (this.memberYn == null) {
            this.memberYn = this.userId != null;
        }
    }
}