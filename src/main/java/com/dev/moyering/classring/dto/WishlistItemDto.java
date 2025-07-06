package com.dev.moyering.classring.dto;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistItemDto {
    private Integer id;
    private String type; //클래스인지 게더링인지
    private String title; //클래스 또는 게더링 이름
    private Date date;
    private String imageUrl;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer userId;
    
    //장소
    private String addr;
    private String detail_addr;
    private String loc_name;
   
    //클래스 또는 게더링 아이디
    private Integer typeId;
    
}
