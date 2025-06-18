package com.dev.moyering.admin.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

// 매핑정보 받는 슈퍼클래스
@Getter
@MappedSuperclass // 속성만 내려주기
public abstract class BaseEntity {
    // 로그인 구현 후 어노테이션 @ 주석 제거
//    @CreatedBy
    private String createdBy = "admin"; // 일단 디폴트 값

    //    @CreatedDate
    private LocalDateTime createdAt;

    //    @LastModifiedBy
    private String modifiedBy = "admin"; // 누가 수정했는지

    //    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        if (this.createdBy == null){
            this.createdBy = "admin";
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.lastModifiedDate = LocalDateTime.now();
        this.modifiedBy = "admin";
    }

}
