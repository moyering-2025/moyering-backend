package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminNotice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminNoticeDto {
    private Integer noticeId;

    @NotEmpty
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;

    @NotEmpty
    @Size(max = 3000, message = "내용은 3000자를 초과할 수 없습니다.")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean pinYn;

    @JsonProperty("isHidden")    // 참고 : Jackson은 Java의 boolean 필드를 JSON으로 변환할 때 자동으로 is 접두사를 제거해서 hidden으로 보여짐
    private boolean isHidden;

    // 리파지토리에서 사용하는 필드 생성자 생성
    public AdminNoticeDto(Integer noticeId, String title, String content,
                     boolean pinYn, boolean isHidden, LocalDateTime createdAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.pinYn = pinYn;
        this.isHidden = isHidden;
        this.createdAt = createdAt;
    }

    // 수정일 포함
    public AdminNoticeDto(Integer noticeId, String title, String content,
                     boolean pinYn, boolean isHidden, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.pinYn = pinYn;
        this.isHidden = isHidden;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // DTO -> Entity 변환 (  * Repository에서 사용하는 생성자로 순서가 Repository 구현과 정확히 일치해야 함)
    public AdminNotice toEntity() {
        return AdminNotice.builder()
                .title(this.title)
                .content(this.content)
                .pinYn(this.pinYn)
                .isHidden(this.isHidden)
                .build();
    }
}




