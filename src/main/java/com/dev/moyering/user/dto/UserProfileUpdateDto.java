package com.dev.moyering.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class UserProfileUpdateDto {
    private String name;
    private String tel;
    private String email;
    private String addr;
    private String detailAddr;
    private Date birthday;
    private String intro;
    //    private List<String> categories;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private String category5;
}
