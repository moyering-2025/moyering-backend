package com.dev.moyering.gathering.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringInquiryRepositoryImpl implements GatheringInquiryRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Autowired
	private ModelMapper modelMapper;
	
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
	    QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
	    QUser user = QUser.user;
	    
	    List<Tuple> tupleList = jpaQueryFactory.select(
	            gatheringInquiry,
	            user.userId,  
	            user.nickName
	        )
	        .from(gatheringInquiry)
	        .leftJoin(user).on(gatheringInquiry.user.userId.eq(user.userId))
	        .where(gatheringInquiry.gathering.gatheringId.eq(gatheringId))
	        .orderBy(gatheringInquiry.inquiryId.desc())
	        .fetch(); 
	        
	    return tupleList.stream()
	        .map(t -> {
	            GatheringInquiry gi = t.get(0, GatheringInquiry.class);
	            Integer userId = t.get(1, Integer.class);
	            String nickName = t.get(2, String.class);
	            
	            // ModelMapper로 기본 매핑 후 수동으로 필요한 필드 설정
	            GatheringInquiryDto dto = modelMapper.map(gi, GatheringInquiryDto.class);
	            dto.setUserId(userId);    
	            dto.setNickName(nickName);  
	            return dto;
	        }).collect(Collectors.toList());
	}
	@Transactional
	public void responseToGatheringInquiry (GatheringInquiryDto gatheringInquiryDto) throws Exception{
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
		JPAUpdateClause clause = jpaQueryFactory.update(gatheringInquiry)
				.set(gatheringInquiry.responseContent, gatheringInquiryDto.getInquiryContent())
				.set(gatheringInquiry.responseDate, gatheringInquiryDto.getResponseDate())
				.where(gatheringInquiry.inquiryId.eq(gatheringInquiryDto.getInquiryId()));
		clause.execute();
	}
}
