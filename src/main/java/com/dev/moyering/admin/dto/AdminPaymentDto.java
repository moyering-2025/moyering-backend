//package com.dev.moyering.admin.dto;
//
//import com.dev.moyering.admin.entity.AdminReport;
//
//import java.sql.Date;
//
//public class AdminPaymentDto {
//    private Integer paymentNo;  // 결제번호 (auto_increment)
//    private String orderNo; // 주문번호 (
//    private String studentId; // 수강생 아이디
//    private Integer classAmount; // 클래스금액
//    private Integer discountAmount; // 할인금액
//    private Integer commission; // 수수료
//    private Integer totalAmount; // 총 결제 금액
//    private String paymentType; // 결제 유형
//    private String status; // 결제 상태
//    private Date payDate; // 결제일시
//
//
//    // DTO -> entity 변환
//    public AdminReport toEntity() {
//        return AdminReport.builder()
//                .reporterId(this.reporterId)
//                .targetOwnerId(this.targetOwnerId)
//                .reportType(this.reportType)
//                .targetId(this.targetId)
//                .title(this.title)
//                .content(this.content)
//                .build();
//    }
//
//}
