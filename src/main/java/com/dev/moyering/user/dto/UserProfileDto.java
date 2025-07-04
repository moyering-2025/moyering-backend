package com.dev.moyering.user.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class UserProfileDto {
    private String name;
    private String tel;
    private String email;
    private String addr;
    private String detailAddr;
    private Date birthday;
    private String intro;
    private List<String> categories;
    private String profile;
    private Integer activeScore;
    private Integer userBadgeId;
}
