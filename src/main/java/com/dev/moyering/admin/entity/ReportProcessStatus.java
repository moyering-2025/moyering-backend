package com.dev.moyering.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportProcessStatus {
    ALL("전체"),
    PENDING("대기중"),
    RESOLVED("처리완료"),
    HIDDEN("숨기기"), // '숨기기' 버튼을 통해 처리됨
    VISIBLE("보이기"); // '보이기' 버튼을 통해 처리됨 (미처리 상태)

    private final String displayName;

    public static ReportProcessStatus fromString(String text) {
        for (ReportProcessStatus b : ReportProcessStatus.values()) {
            if (b.displayName.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null; // 또는 IllegalArgumentException 발생
    }
}