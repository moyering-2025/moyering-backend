package com.dev.moyering.admin.dto;

import java.sql.Date;

public class AdminClassDetailDto {
    private String portfolioDownload;
    private String materialDownload;
    private String firstCategory;
    private String secondCategory;
    private Integer classPrice;
//    private min



    // 클래스 관리 페이지 상세
    private String classStatus ; // 클래스 상태 (class_calendar.status) => 대기 / 승인 / 거절 상태 띄우고, 세부 상태 보여주기 (승인 하면 바로 모집 중으로 상태 변경, 모집마감 / 폐강)
    private String name; //스케줄 명
    private String content; // 클래스 내용
    private String registered_count; //등록인원
    private String email;


    // 강의 상세페이지를 보여주고 승인 등록 버튼 클릭할 수 있도록 하기!
    // 클래스 횟수 ? 를 보여주기  (몇회차인지??? )
    private Date startDate; // 클래스 일자
    // 클래스 관리 페이지에서는 승인, 거절만 할 수 있도록 설계하기 ! (클래스 날짜)
    // (상세쪽에서 개강, 폐강, 모집중, 모집마감 상태관리) => 클래스 날짜 여러개 보여주기
    // 상세페이지에 수강인원, 각 클래스 날짜, 상태 관리하기
    // 등록인원(registered_People)
}
