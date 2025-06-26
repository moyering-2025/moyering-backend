package com.dev.moyering.classring.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.classring.dto.ClassLikesDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classLikeId;
    
    @Column (nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="class_id", nullable = false)
	private HostClass hostClass;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	public ClassLikesDto toDto() {
		return ClassLikesDto.builder()
				.classLikeId(classLikeId)
				.createdAt(createdAt)
				.classId(hostClass!=null ? hostClass.getClassId():null)
				.className(hostClass!=null? hostClass.getName():null)
				.userId(user!=null?user.getUserId():null)
				.build();
	}

}
