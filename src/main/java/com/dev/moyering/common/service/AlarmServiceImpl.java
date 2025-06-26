package com.dev.moyering.common.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.entity.Alarm;
import com.dev.moyering.common.repository.AlarmRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.util.PageInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;
    private final FirebaseMessaging firebaseMessaging;
	private final UserRepository userRepository;

	//프론트에서 받은 fcmToken DB에 저장
	@Override
	public void registFcmToken(Integer userId, String fcmToken) {
		Optional<User> ouser = userRepository.findById(userId);
		if(ouser.isEmpty()) {
			System.out.println("사용자오류");
			return;
		}
		
		ouser.get().setFcmToken(fcmToken);
		userRepository.save(ouser.get());
	}

	@Override
	public List<AlarmDto> findAlarmListByReceiverUserId(PageInfo pageInfo, Map<String, Object> param) throws Exception {

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		Long cnt = alarmRepository.countAlarmsByReceiverUserId(param);
		
		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return alarmRepository.findAlarmListByReceiverUserId(pageRequest, param);
	}

	@Override
	public Boolean sendAlarm(AlarmDto alarmDto) throws Exception {
		System.out.println(alarmDto);
		  //'1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
		
		//1. username로 fcmToken 가져오기
		Optional<User> ouser = userRepository.findById(alarmDto.getReceiverUserId());
		if(ouser.isEmpty()) {
			System.out.println("사용자 오류");
			return false;
		}
		String fcmToken = ouser.get().getFcmToken();
		if(fcmToken==null || fcmToken.trim().length()==0) {
			System.out.println("FCM Token 오류");
			return false;
		}
		//2. AlarmTable에 저장
		Alarm alarm =  Alarm.builder().receiver(User.builder().userId(alarmDto.getReceiverUserId()).build())
			.sender(User.builder().userId(alarmDto.getSenderUserId()).build())
			.senderNickName(alarmDto.getSenderUserNickName())
			.title(alarmDto.getTitle())
			.content(alarmDto.getContent())
			.confirm(false).build();
			alarmRepository.save(alarm);
				
		//3. FCM 메시지 전송
//		Notification notification = Notification.builder()
//				.setTitle(alarm.getAlarmId()+"_"+alarmDto.getTitle())
//				.setBody(alarmDto.getContent())
//				.build();
				
		Message message = Message.builder()
				.setToken(fcmToken)
//						.setNotification(notification)
				.putData("num", alarmDto.getAlarmId()+"")
				.putData("title", alarmDto.getTitle())
				.putData("body", alarmDto.getContent())
				.putData("sender", alarmDto.getSenderUserNickName())
				.putData("receiver", ouser.get().getNickName())
				.build();
			
			try {
				firebaseMessaging.send(message);
				return true;
			} catch(FirebaseMessagingException e) {
				e.printStackTrace();
				return false;
			}
	}
		
	
	//특정알람 확인(알람번호)
	@Override
	public Boolean confirmAlarm(Integer alarmNum) {
		Optional<Alarm> oalarm = alarmRepository.findById(alarmNum);
		System.out.println(oalarm.get());
		if(oalarm.isEmpty()) {
			System.out.println("알람번호 오류");
			return false;
		}
		Alarm alarm = oalarm.get();
		alarm.setConfirm(true);
		alarmRepository.save(alarm);
		return true;
	}
		
		//알람목록 확인(알람번호)
	@Override
	public Boolean confirmAlarmAll(List<Integer> alarmList) {
		for(Integer num : alarmList) {
			Optional<Alarm> oalarm = alarmRepository.findById(num);
			if(oalarm.isPresent()) {
				Alarm alarm = oalarm.get(); 
				alarm.setConfirm(true);
				alarmRepository.save(alarm);
			}
		}
		return true;
	}
}
