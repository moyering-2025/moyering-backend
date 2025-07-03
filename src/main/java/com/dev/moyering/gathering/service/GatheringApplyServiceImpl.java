package com.dev.moyering.gathering.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.util.PageInfo;
@Service
public class GatheringApplyServiceImpl implements GatheringApplyService {

	@Autowired
	public GatheringApplyRepository gatheringApplyRepository;
	
	@Autowired
	private GatheringRepository gatheringRepository;
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Integer findApprovedUserCountByGatheringId(Integer gatheringId) throws Exception {
		return gatheringApplyRepository.findApprovedUserCountByGatheringId(gatheringId);
	}
	@Override
	public List<GatheringApplyDto> findApprovedUserListByGatheringId(Integer gatheringId) throws Exception {
		//상세보기용
		return gatheringApplyRepository.findApprovedUserListByGatheringId(gatheringId);
	}
	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception {
		//주최자 시점
		return gatheringApplyRepository.findApplyUserListByGatheringIdForOrganizer(gatheringId);
	}

	@Override
	public Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception {
		//상세보기용, 신청여부 조회 
		return gatheringApplyRepository.findByGatheringIdAndUserId(gatheringId, userId);
	} 
	@Override
	public Integer applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception {
		
		GatheringApply gatheringApply = gatheringApplyDto.toEntity();
		gatheringApplyRepository.save(gatheringApply);
		Integer no = gatheringApply.getGatheringApplyId();
		
		Gathering gathering = gatheringRepository.findById(gatheringApplyDto.getGatheringId()).get();
//		User user =userRepository.findById(gatheringApplyDto.getUserId()).get(); 
		
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(3)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("모임 신청자 접수") // 필수 사항
				.receiverId(gathering.getUser().getUserId())
				//수신자 유저 아이디
				.senderId(gatheringApplyDto.getUserId())
				//발신자 유저 아이디 
				.senderNickname(gatheringApplyDto.getNickName())
				//발신자 닉네임 => 시스템/관리자가 발송하는 알람이면 메니저 혹은 관리자, 강사가 발송하는 알람이면 강사테이블의 닉네임, 그 외에는 유저 테이블의 닉네임(마이페이지 알림 내역에서 보낸 사람으로 보여질 이름)
				.content(gatheringApplyDto.getTitle() +"에 "+gatheringApplyDto.getNickName()+"님이 참여 신청하였습니다")//알림 내용
				.build();
		System.out.println("알람 보내기 테스트 "+ alarmDto);
		alarmService.sendAlarm(alarmDto);
		
		return no;
	}

	@Override
	public void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved) throws Exception {
		//주최자 시점 수락여부 결정
		gatheringApplyRepository.updateGatheringApplyApproval(gatheringApplyId, isApproved);

		Optional<GatheringApply> oGatheringApply = gatheringApplyRepository.findById(gatheringApplyId);
		System.out.println("oGatheringApply : "+oGatheringApply);
		if(isApproved) {
			
		}else {
			
		}
//		AlarmDto alarmDto = AlarmDto.builder()
//				.alarmType(3)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
//				.title("?? 신청 접수 관련 안내") // 필수 사항
//				.receiverId(oGatheringApply.get)
//				//수신자 유저 아이디
//				.senderId(gatheringApplyDto.getUserId())
//				//발신자 유저 아이디 
//				.senderNickname(gatheringApplyDto.getNickName())
//				//발신자 닉네임 => 시스템/관리자가 발송하는 알람이면 메니저 혹은 관리자, 강사가 발송하는 알람이면 강사테이블의 닉네임, 그 외에는 유저 테이블의 닉네임(마이페이지 알림 내역에서 보낸 사람으로 보여질 이름)
//				.content(gatheringApplyDto.getTitle() +"에 "+gatheringApplyDto.getNickName()+"님이 가입하였습니다")//알림 내용
//				.build();
//		alarmService.sendAlarm(alarmDto);
	}
	@Override
	public List<GatheringApplyDto> findApplyListByApplyUserId(Integer userId, PageInfo pageInfo, String word, String status) throws Exception {
	    // 내가 지원한 게더링 목록 + 페이지네이션, 제목으로 검색 
	    PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
	    Long cnt = gatheringApplyRepository.findMyApplyListCount(userId, word, status);
	    
	    Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
	    Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
	    Integer endPage = Math.min(startPage+10-1, allPage);
	    
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
	    List<GatheringApplyDto> gatheringApplyList = gatheringApplyRepository.getAppliedGatheringList(userId, word, status, pageRequest);
	    
	    // 2. 각 게더링별 참여 중인 인원수 정보 추가
	    for (GatheringApplyDto gatheringApplyDto : gatheringApplyList) {
	        Integer gatheringId = gatheringApplyDto.getGatheringId();
	        Integer acceptedCount = gatheringApplyRepository.findApprovedUserCountByGatheringId(gatheringId);
	        gatheringApplyDto.setAcceptedCount(acceptedCount != null ? acceptedCount : 0);
	    }
	    return gatheringApplyList;
	}
	@Override
	public Integer selectMyApplyListCount(Integer userId, String word, String status) throws Exception {
		// TODO Auto-generated method stub
		return gatheringApplyRepository.findMyApplyListCount(userId, word, status).intValue();
	}
	@Override
	 public void cancelGatheringApply(Integer gatheringApplyId) throws Exception {
		if (!gatheringApplyRepository.existsById(gatheringApplyId)) {
           throw new Exception("GatheringApplyId " + gatheringApplyId + "를 찾을 수 없습니다.");
       }
       gatheringApplyRepository.deleteById(gatheringApplyId);
	}
}
