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
@AllArgsConstructor
public class AdminClassDto {
    //클래스 관리 페이지
    private Integer classId;        // 클래스 아이디 (HostClass.class_id)
    private String firstCategory;   // 1차 카테고리명 (Category.categoryName)
    private String secondCategory;  // 2차 카테고리명 (SubCategory.subCategoryName)
    private Integer hostId;         // 강사 아이디 (Host.host_id)
    private String hostName;        // 강사 이름 (Host.name)
    private String className;       // 클래스명 (HostClass.name)
    private Integer price;          // 가격 (HostClass.price)
    private Integer recruitMin;     // 등록 최소인원 (HostClass.recruitMin)
    private Integer recruitMax;     // 등록 최대인원 (HostClass.recruitMax)
    private Date regDate;           // 클래스 개설일자 (HostClass.regDate)
    private String processStatus;   // 클래스 상태 (ClassCalendar.status) => 대기, 승인, 거절

    // 추가 필드들 (필요에 따라)
    private Integer subCategoryId;  // SubCategory ID (toEntity에서 필요)
    private Integer calendarId;     // ClassCalendar ID (필요시)

    // 클래스 엔티티로 변환
    public HostClass toEntity() {
        // Host 엔티티 생성 (ID만으로 - 실제 DB 조회는 서비스에서)
        Host host = Host.builder()
                .hostId(this.hostId)
                .name(this.hostName)  // 이름도 설정 (선택사항)
                .build();

        // SubCategory는 ID로만 생성 (실제 조회는 서비스에서)
        SubCategory subCategory = SubCategory.builder()
                .subCategoryId(this.subCategoryId)
                .subCategoryName(this.secondCategory)
                .build();

        return HostClass.builder()
                .classId(this.classId)
                .host(host)
                .subCategory(subCategory)
                .name(this.className)
                .price(this.price)
                .recruitMin(this.recruitMin)
                .recruitMax(this.recruitMax)
                .regDate(this.regDate)
                .build();
    }

    // 더 안전한 toEntity - 실제 엔티티 조회 후 생성
    public HostClass toEntityWithValidation(Host host, SubCategory subCategory) {
        return HostClass.builder()
                .classId(this.classId)
                .host(host)           // 실제 조회된 Host
                .subCategory(subCategory)  // 실제 조회된 SubCategory
                .name(this.className)
                .price(this.price)
                .recruitMin(this.recruitMin)
                .recruitMax(this.recruitMax)
                .regDate(this.regDate)
                .build();
    }
}