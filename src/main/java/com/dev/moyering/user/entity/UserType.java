package com.dev.moyering.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_MG("관리자"), // 필터용
    ROLE_MB("일반"),
    ROLE_HT("강사");

    private final String displayName;
}