package com.dev.moyering.host.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRegistDto {
    private Integer sudentId;
    private Integer attCount;
    private Integer userId;
    private Integer calendarId;
    
    //부가정보
    private String classTitle;      
    private String userName;   
    private Date startDate;
    private String addr;
    private String refund;
    private Integer recruit_min;
    private Integer recruit_max;
}
