package com.dev.moyering.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.dev.moyering.entity.user.Gathering;
import com.dev.moyering.entity.user.QGathering;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class GatheringRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public Gathering selectGathering(Integer gatheringId) throws Exception{
		QGathering gathering = QGathering.gathering;
		return jpaQueryFactory.selectFrom(gathering)
				.where(gathering.gatheringId.eq(gatheringId))
				.fetchOne();
	}
	public List<Gathering> selectMyGatheringList(PageRequest pageRequest, String word){	
		QGathering gathering = QGathering.gathering;
		List<Gathering> gatheringList = null;
		if(word==null || word.trim().length()==0) {
			return jpaQueryFactory.selectFrom(gathering)
					.orderBy(gathering.gatheringId.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		}
		return null;
		
	}
	public List<Gathering> selectArticleListByPaging(PageRequest pageRequest, String type, String word) {
		return null;
	
	}
}
