package com.dev.moyering.admin.entity;

import javax.persistence.*;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자, 빈 객체 생성 차단
@Entity
@ToString
@Table(name = "notice")
public class AdminNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer noticeId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "author_id")
//    private User author; // 작성자 (관리자)


    // NOTNULL, 길이 200자 이하
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Lob
    private String content;

    @Column(nullable = false)
    private boolean pinYn;


    @Column(nullable = false)
    private boolean isHidden;

    // 변경 가능한 필드를 생성자 통해 변경
    @Builder
    public AdminNotice(String title, String content, boolean pinYn, boolean isHidden){
        this.title = title;
        this.content = content;
        this.pinYn = pinYn;
        this.isHidden = isHidden;
// createdDate, lastModifiedDate는 BaseEntity에서 자동 처리
    }

    // 엔티티 -> toDto
    public AdminNoticeDto toDto() {
        return AdminNoticeDto.builder()
                .noticeId(this.noticeId)
                .title(this.title)
                .content(this.content)
                .pinYn(this.pinYn)
                .isHidden(this.isHidden)
                .createdAt(this.getCreatedAt())        // BaseEntity에서 가져옴
                .updatedAt(this.getLastModifiedDate())
                .build();
    }

    // 공지사항 변경 (제목, 내용)
    public void changeNotice(String title, String content){
        this.title = title;
        this.content = content;
    }

    // 공지사항 메인 > 핀 상태 변경
    public void changePinStatus(boolean pinYn){
        this.pinYn = pinYn;
    }


    // 또는 명시적으로 숨기기/보이기 (삭제할 때 사용)
    public void hide() {
        this.isHidden = true;
    }

    public void show() {
        this.isHidden = false;
    }

    // 핀 설정
    public boolean isPinned(){
        return this.pinYn;
    }
}
