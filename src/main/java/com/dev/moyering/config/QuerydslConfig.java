package com.dev.moyering.config;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class QuerydslConfig {
	@Autowired
	EntityManager entityManager;
	
	//쿼리dsl을 사용하기 위해서는 JPAQueryFactor가 필요하고 그러기 위해서는 entityManager가 필요하다.
	//빈등록하면 스프링프레임워크가 관리해줌
	@Bean //모든 서블릿에서 함께 사용하는 객체는 빈으로 등록해주면 공유 가능
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
	
	@Bean 
	public ModelMapper modelMapper() {
		return new ModelMapper(); 
	}
}
