package com.dev.moyering.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    MANAGER("ROLE_MG", "관리자"),
    MEMBER("ROLE_MB", "일반"),
    HOST("ROLE_HT", "강사");

    private final String code;
    private final String displayName;

    // 코드로 Enum 찾기
    public static UserType fromCode(String code) {
        for (UserType type : UserType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("알 수 없는 타입코드 :" + code);
    }

    // 화면용 텍스트로 Enum 찾기 > 관리자 회원관리
    public static UserType fromDisplayName(String displayName) {
        for (UserType type : UserType.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("알 수 없는 display name :" + displayName);
    }
}