package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder

public class NoticeDto {
    private Integer noticeId;

//    @NotBlank("제목은 필수입니다.") // 유효성 검사
//    @Size(max = 100, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;

//    @NotBlank("내용은 필수입니다.")
//    @Size(max = 3000, message = "내용은 3000자를 초과할 수 없습니다.")
    private String content;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean pinYn;
    private boolean isHidden;

    // DTO -> Entity 변환
    // 클래스 이름 바로 호출할 수 있도록 static 사용
    public static NoticeDto from(Notice notice){
        return NoticeDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .pinYn(notice.isPinned())
                .isHidden(!notice.isVisible())
                .build();
    }
}


