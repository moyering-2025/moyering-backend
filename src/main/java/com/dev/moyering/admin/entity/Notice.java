package com.dev.moyering.admin.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자, 빈 객체 생성 차단
@Entity
@ToString
public class Notice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer noticeId;

    // NOTNULL, 길이 200자 이하
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean pinYn;

    @Column(nullable = false)
    private boolean isHidden;

    // 변경 가능한 필드를 생성자 통해 변경
    @Builder
    public Notice(String title, String content, boolean pinYn, boolean isHidden){
        this.title = title;
        this.content = content;
        this.pinYn = pinYn;
        this.isHidden = isHidden;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 공지사항 변경
    public void changeNotice(String title, String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 수정시간도 함께 업데이트
    }

    // 핀 상태 변경
    public void changePinStatus(boolean pinYn){
        this.pinYn = pinYn;
        this.updatedAt = LocalDateTime.now();
    }

    // 숨기기, 보이이 상태 변경
    public boolean isVisible(){
        return !this.isHidden; // 클릭하면 숨기기 -> 보이기, 보이기 -> 숨기기
    }

    // 핀 설정
    public boolean isPinned(){
        return this.pinYn;
    }
}
