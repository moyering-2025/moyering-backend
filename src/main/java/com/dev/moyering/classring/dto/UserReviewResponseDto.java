package com.dev.moyering.classring.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewResponseDto {
    // 공통
    private Integer reviewId;
    private String classTitle;
    private Date classDate;

    // 리뷰 정보
    private Date reviewDate;
    private Integer star;
    private String content;

    // 강사 답변
    private String teacherReply;
    private Date responseDate;
}
