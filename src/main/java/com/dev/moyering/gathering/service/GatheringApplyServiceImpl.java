package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
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
	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception {
		//상세보기용
		return gatheringApplyRepository.findApplyUserListByGatheringId(gatheringId);
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
		User user =userRepository.findById(gatheringApplyDto.getUserId()).get(); 
		
		AlarmDto alarmDto = AlarmDto.builder()
				.alarmType(3)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
				.title("모임 신청자 접수") // 필수 사항
				.receiverId(gathering.getUser().getUserId())
				//수신자 유저 아이디
				.senderId(gatheringApplyDto.getUserId())
				.senderNickname(user.getNickName())
				.content(gatheringApplyDto.getTitle() +"에 "+user.getNickName()+"이 가입하였습니다")
				.build();
		
		alarmService.sendAlarm(alarmDto);
		
		return no;
	}

	@Override
	public void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved) throws Exception {
		//주최자 시점 수락여부 결정
		gatheringApplyRepository.updateGatheringApplyApproval(gatheringApplyId, isApproved);
	}
	@Override
	public List<GatheringApplyDto> findApplyListByApplyUserId(Integer userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
