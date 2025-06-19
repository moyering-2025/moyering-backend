package com.dev.moyering.admin.dto;

import java.util.Date;

import com.dev.moyering.user.entity.User;

import lombok.*;

@Getter
@NoArgsConstructor
//@AllArgsConstructor // queryDSL의 Projections.constructor로 대체
@Builder
public class AdminMemberDto {
    private Integer userId; // auto_increment key
    private String userType; // 회원 구분
    private String username; // 회원 아이디
    private String name; // 이름
    private String email; // 이메일
    private String tel; // 연락처
    private Date regDate; // 가입일
    private String useYn; // 사용여부

    // 매핑용 생성자
    public AdminMemberDto(Integer userId, String userType, String username,
                          String name, String email, String tel,
                          Date regDate, String useYn) {
        this.userId = userId;
        this.userType = userType;
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
                .userType(userType)
                .username(username)
                .name(name)
                .email(email)
                .tel(tel)
                .regDate(regDate)
                .useYn(useYn)
                .build();
    }
}