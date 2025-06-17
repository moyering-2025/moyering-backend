package com.dev.moyering.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    ALL("전체"), // 필터용
    POST("게시글"),
    COMMENT("댓글"),
    USER("사용자"),
    CLASS("강의");
    private final String displayName; // UI에 표시될 이름

    // 문자열로부터 ReportType을 찾아주는 헬퍼 메서드 (선택 사항)
    public static ReportType fromString(String text) {
        for (ReportType b : ReportType.values()) {
            if (b.displayName.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null; // 또는 IllegalArgumentException 발생
    }
}