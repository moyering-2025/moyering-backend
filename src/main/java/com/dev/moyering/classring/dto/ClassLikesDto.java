package com.dev.moyering.classring.dto;

import java.time.LocalDateTime;

import com.dev.moyering.classring.entity.ClassLikes;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassLikesDto {
    private Integer classLikeId;
    private LocalDateTime createdAt;
    private Integer classId;
    private Integer userId;
    
    //부가정보
    private String className;
    
    public ClassLikes toEntity() {
    	return ClassLikes.builder()
    			.classLikeId(classLikeId)
    			.hostClass(classId!=null? HostClass.builder().classId(classId).build():null)
    			.user(userId!=null? User.builder().userId(userId).build():null)
    			.build();
    }
}
