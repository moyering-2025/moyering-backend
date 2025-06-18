package com.dev.moyering.admin.dto;

import java.sql.Date;

import com.dev.moyering.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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


    public User toEntity() {
        return User.builder()
                .userId(userId)
                .id(username)
                .name(name)
                .tel(tel)
                .useYn(useYn)
                .userType(userType)
                .email(email)
                .regDate(regDate)
                .build();
    }
}