package com.dev.moyering.admin.dto;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.HostClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@Builder
@NoArgsConstructor

public class AdminClassDto {
    private Integer classId;        // 클래스 아이디
    private String firstCategory;   // 1차 카테고리명
    private String secondCategory;  // 2차 카테고리명
    private Integer hostId;         // 강사 아이디
    private String hostName;        // 강사 이름
    private String className;       // 클래스명
    private Integer price;          // 가격
    private Integer recruitMin;     // 등록 최소인원
    private Integer recruitMax;     // 등록 최대인원
    private Date regDate;           // 클래스 개설일자
    private String processStatus;   // 클래스 상태 (대기, 승인, 거절)

    // Projections.constructor에서 사용할 생성자 (필드 순서 중요!)
    public AdminClassDto(Integer classId, String firstCategory, String secondCategory,
                         Integer hostId, String hostName, String className,
                         Integer price, Integer recruitMin, Integer recruitMax,
                         Date regDate, String processStatus) {
        this.classId = classId;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.hostId = hostId;
        this.hostName = hostName;
        this.className = className;
        this.price = price;
        this.recruitMin = recruitMin;
        this.recruitMax = recruitMax;
        this.regDate = regDate;
        this.processStatus = processStatus;
    }

    // 기존 toEntity 메서드들은 그대로 유지...
    public HostClass toEntity() {
        Host host = Host.builder()
                .hostId(this.hostId)
                .name(this.hostName)
                .build();

        return HostClass.builder()
                .classId(this.classId)
                .host(host)
                .name(this.className)
                .price(this.price)
                .recruitMin(this.recruitMin)
                .recruitMax(this.recruitMax)
                .regDate(this.regDate)
                .build();
    }
}
