package com.dev.moyering.gathering.repository;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.user.entity.QUser;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Override
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
				.set(gathering.tags, gatheringDto.getTags())
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
	@Override
	public void updateGatheringStatus(Integer gatheringId, String status) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.status, status)
				.where(gathering.gatheringId.eq(gatheringId));
		clause.execute();
	}
	@Override
	public List<Gathering> findRecommendGatherRingForUser(User user) throws Exception {
		return null;
	}
  
  @Override
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
  
  public List<GatheringDto> selectMyGatheringListByPaging(
      PageRequest pageRequest, Integer userId, String searchTitle) {
      
      QGathering gathering = QGathering.gathering;
      QUser user = QUser.user;
      
      List<Tuple> tupleList = null;
      
      if(searchTitle == null || searchTitle.trim().length() == 0) {
          tupleList = jpaQueryFactory.select(gathering, user.nickName, user.profile, user.intro)
                      .from(gathering)
                      .leftJoin(user)
                      .on(gathering.user.userId.eq(user.userId))
                      .where(gathering.user.userId.eq(userId))
                      .orderBy(gathering.createDate.desc())
                      .offset(pageRequest.getOffset())
                      .limit(pageRequest.getPageSize())
                      .fetch();
      } else {
          tupleList = jpaQueryFactory.select(gathering, user.nickName, user.profile, user.intro)
                      .from(gathering)
                      .leftJoin(user)
                      .on(gathering.user.userId.eq(user.userId))
                      .where(gathering.user.userId.eq(userId)
                             .and(gathering.title.contains(searchTitle)))
                      .orderBy(gathering.createDate.desc())
                      .offset(pageRequest.getOffset())
                      .limit(pageRequest.getPageSize())
                      .fetch();
      }
      
      return tupleList.stream()
              .map(t -> {
                  Gathering g = t.get(0, Gathering.class);
                  String nickName = t.get(1, String.class);
                  String profile = t.get(2, String.class);
                  String intro = t.get(3, String.class);
                  
                  GatheringDto gatheringDto = g.toDto();
                  gatheringDto.setNickName(nickName);
                  gatheringDto.setProfile(profile);
                  gatheringDto.setIntro(intro);
                  
                  return gatheringDto;
              }).collect(Collectors.toList());
  }
  @Override
  public Map<String, Object> selectGatheringWithApplicationsByUserIdAndPaging(
	    PageRequest pageRequest, Integer userId, String searchTitle) {
	    // 게더링 목록 조회
	    List<GatheringDto> gatheringList = selectMyGatheringListByPaging(pageRequest, userId, searchTitle);
	    
	    // 각 게더링별 지원내역 조회
	    Map<Integer, List<GatheringApplyDto>> applicationMap = new HashMap<>();
	    for(GatheringDto gathering : gatheringList) {
	        List<GatheringApplyDto> applications;
			try {
				GatheringApplyRepositoryImpl gatheringApplyRepository = new GatheringApplyRepositoryImpl(jpaQueryFactory);
				applications = gatheringApplyRepository.selectApplicationsByGatheringId(gathering.getGatheringId());
				applicationMap.put(gathering.getGatheringId(), applications);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    Map<String, Object> result = new HashMap<>();
	    result.put("gatheringList", gatheringList);
	    result.put("applicationMap", applicationMap);
	    
	    return result;
	}
  
}
