package com.dev.moyering.user.dto;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserProfileUpdateDto {
    private String name;
    private String tel;
    private String email;
    private String addr;
    private String detailAddr;
    private Date birthday;
    private String intro;
    private List<String> categories;
}
