package com.dev.moyering.admin.dto;

import java.sql.Date;

import com.dev.moyering.user.entity.User;

import lombok.*;

@Getter
@NoArgsConstructor
//@AllArgsConstructor // queryDSL의 Projections.constructor로 대체
public class AdminMemberDto {
    private Integer userId; // auto_increment key
    private String userType; // 회원 구분
    private String userTypeCode; // 원본 코드값 (ROLE_MG, ROLE_MB, ROLE_HT)
    private String username; // 회원 아이디
    private String name; // 이름
    private String email; // 이메일
    private String tel; // 연락처
    private Date regDate; // 가입일
    private String useYn; // 사용여부

    // 매핑용 생성자
    public AdminMemberDto(Integer userId, String userTypeCode, String username,
                          String name, String email, String tel,
                          Date regDate, String useYn) {
        this.userId = userId;
        this.userTypeCode = userTypeCode; // 원본 코드 저장
        this.userType = convertUserTypeToDisplayText(userTypeCode); // 화면용 텍스트로 변환
        this.username = username;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.regDate = regDate;
        this.useYn = useYn;
    }


    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userType(userTypeCode)
                .username(username)
                .name(name)
                .email(email)
                .tel(tel)
                .regDate(regDate)
                .useYn(useYn)
                .build();
    }

    // userType 변환
    private String convertUserTypeToDisplayText(String userTypeCode) {
        if (userTypeCode == null) {
            return "알 수 없음";
        }

        switch (userTypeCode) {
            case "ROLE_MG":
                return "관리자";
            case "ROLE_MB":
                return "일반";
            case "ROLE_HT":
                return "강사";
            default:
                return "알 수 없음";
        }
    }
}