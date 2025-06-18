package com.dev.moyering.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ALL("전체"), // 필터용
    HOST("강사"),
    USER("일반사용자"),
    MGR("관리자");

    private final String displayName;
}