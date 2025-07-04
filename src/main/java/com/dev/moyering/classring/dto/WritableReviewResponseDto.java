package com.dev.moyering.classring.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritableReviewResponseDto {
    private String classTitle;
    private Date classDate;
    private Integer calendarId;
    private Integer userId;
    private Integer hostId;
}
