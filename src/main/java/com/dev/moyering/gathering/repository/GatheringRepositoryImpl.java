package com.dev.moyering.gathering.repository;

import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.QGathering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	
	public Long selectMyGatheringListCount(PageRequest pageRequest, Integer loginId, String word){	
		QGathering gathering = QGathering.gathering;
		if(word==null || word.trim().length()==0) {//검색어 없는 경우
			return jpaQueryFactory.select(gathering.count())
					.where(gathering.user.userId.eq(loginId)
							.and(gathering.title.contains(word))) // 제목에서 검색
					.from(gathering)
					.fetchOne();
		}
		Long cnt = 0L;
		cnt = jpaQueryFactory.select(gathering.count())
				.where(gathering.user.userId.eq(loginId)
						.and(gathering.title.contains(word))) // 제목에서 검색
				.from(gathering)
				.fetchOne();
		return cnt;
	}
	
	@Transactional
	public void updateGathering(GatheringDto gatheringDto) throws Exception{
		QGathering gathering = QGathering.gathering;
		LocalTime startTime = LocalTime.parse(gatheringDto.getStartTime());
		LocalTime endTime = LocalTime.parse(gatheringDto.getEndTime());
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.title, gatheringDto.getTitle())
				.set(gathering.gatheringContent, gatheringDto.getGatheringContent())
				.set(gathering.tags, gatheringDto.getAddress())
				.set(gathering.subCategory.subCategoryId, gatheringDto.getSubCategoryId())
				.set(gathering.meetingDate, gatheringDto.getMeetingDate())
				.set(gathering.address, gatheringDto.getAddress())
				.set(gathering.detailAddress, gatheringDto.getDetailAddress())
				.set(gathering.preparationItems, gatheringDto.getPreparationItems())
				.set(gathering.intrOnln, gatheringDto.getIntrOnln())//한줄 소개
				.set(gathering.minAttendees, gatheringDto.getMinAttendees())
				.set(gathering.maxAttendees, gatheringDto.getMaxAttendees())
				.set(gathering.startTime, startTime)
				.set(gathering.endTime, endTime)
				.set(gathering.applyDeadline, gatheringDto.getApplyDeadline())
				.where(gathering.gatheringId.eq(gatheringDto.getGatheringId()));
		if(gatheringDto.getThumbnailFileName()!=null && !gatheringDto.getThumbnailFileName().equals("")) {
			clause.set(gathering.thumbnail, gatheringDto.getThumbnailFileName());
		}
		clause.execute();
	}
	/*
	 * 
	public Integer selectBoardLike(String id, Integer num) throws Exception {
		QBoardLike boardLike = QBoardLike.boardLike;
		return jpaQueryFactory.select(boardLike.num)
					.from(boardLike)
					.where(boardLike.memberId.eq(id).and(boardLike.articleNum.eq(num)))
					.fetchOne();
	}
	*//*
	 * 
	public Integer selectBoardTotalLike( Integer num) throws Exception {
		QBoardLike boardLike = QBoardLike.boardLike;
		return jpaQueryFactory.select(boardLike.num)
					.from(boardLike)
					.where(boardLike.memberId.eq(id).and(boardLike.articleNum.eq(num)))
					.fetchOne();
	}
	*/
	public void updateGatheringStatus(Integer gatheringId, String status) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.status, status)
				.where(gathering.gatheringId.eq(gatheringId));
		clause.execute();
	}
	public List<Gathering> selectMyGatheringList(PageRequest pageRequest, Integer loginId, String word){	
		QGathering gathering = QGathering.gathering;
		List<Gathering> gatheringList = null;
		if(word==null || word.trim().length()==0) {//검색어 없는 경우
			gatheringList = jpaQueryFactory.selectFrom(gathering)
					.where(gathering.user.userId.eq(loginId))//등록한 사람이 로그인 아이디와 일치하는 경우에만.
					.orderBy(gathering.gatheringId.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else { // 검색어 있는 경우
		    gatheringList = jpaQueryFactory.selectFrom(gathering)
		            .where(gathering.user.userId.eq(loginId)
		                .and(gathering.title.contains(word))) // 제목에서 검색
		            .orderBy(gathering.gatheringId.desc())
		            .offset(pageRequest.getOffset())
		            .limit(pageRequest.getPageSize())
		            .fetch();
		}
		return gatheringList;
	}
	/*
	 * 
	 * public List<ArticleDto> selectArticleListByPaging(PageRequest pageRequest, String type, String word) {
		QArticle article = QArticle.article;
		QMember member = QMember.member;
		List<Tuple> tupleList = null;
		
		if(word==null || word.trim().length()==0) {
			tupleList = jpaQueryFactory.select(article, member.name)
						.from(article)
						.leftJoin(member)
						.on(article.writer.eq(member.id))
						.orderBy(article.num.desc())
						.offset(pageRequest.getOffset())
						.limit(pageRequest.getPageSize())
						.fetch();
		} else {
		
			if (type.equals("title")) {
				tupleList = jpaQueryFactory.select(article,member.name)
						.from(article)
						.leftJoin(member)
						.on(article.writer.eq(member.id))
						.where(article.title.contains(word))
						.orderBy(article.num.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
						.fetch();
			} else if (type.equals("content")) {
				tupleList = jpaQueryFactory.select(article,member.name)
						.from(article)
						.leftJoin(member)
						.on(article.writer.eq(member.id))
						.where(article.content.contains(word))
						.orderBy(article.num.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
						.fetch();

			} else if (type.equals("writer")) {
				tupleList = jpaQueryFactory.select(article,member.name)
						.from(article)
						.leftJoin(member)
						.on(article.writer.eq(member.id))
						.where(member.name.contains(word))
						.orderBy(article.num.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
						.fetch();
			}
		}
		return tupleList.stream()
				.map(t-> {
					Article a = t.get(0,Article.class);
					String name = t.get(1,String.class);
					ArticleDto articleDto = modelMapper.map(a, ArticleDto.class);
					articleDto.setName(name);
					return articleDto;
				}).collect(Collectors.toList());
	}*/
}
