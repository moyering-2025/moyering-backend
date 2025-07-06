package com.dev.moyering.classring.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilSearchDto {
    private String tab; 
    private int page = 0;
    private int size = 10;

    private Date  startDate; // 최소 날짜
    private Date endDate;   // 최대 날짜

    private Integer userId;
    private String keywords; //혹사라도 검색어 
   
}
