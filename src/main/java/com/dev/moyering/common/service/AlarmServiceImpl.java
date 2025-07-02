package com.dev.moyering.common.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public List<AlarmDto> findAlarmListByReceiverUserId(PageInfo pageInfo, Integer loginId, Integer alarmType,
			Date startDate, Date endDate, Boolean isConfirmed) throws Exception {

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		Long cnt = countAlarmsByReceiverUserId(loginId, alarmType, startDate, endDate, isConfirmed);

		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return alarmRepository.findAlarmListByReceiverUserId(pageRequest, loginId, alarmType, startDate, endDate, isConfirmed);
	}
	@Override
	public List<AlarmDto> getAlarmList(Integer loginId) throws Exception {
		List<Alarm> alarmList = alarmRepository.findByReceiverIdAndConfirmFalse(loginId);
		return alarmList.stream().map(alarm->AlarmDto.builder()
									.alarmId(alarm.getAlarmId())
									.receiverId(loginId)
									.senderId(alarm.getSenderId())
									.senderNickname(alarm.getSenderNickname())
									.title(alarm.getTitle())
									.content(alarm.getContent()).build())
								    .collect(Collectors.toList());
		
	}
	@Override
	public Long countAlarmsByReceiverUserId(Integer loginId, Integer alarmType, Date startDate, Date endDate, Boolean isConfirmed) throws Exception {
		return alarmRepository.countAlarmsByReceiverUserId(loginId, alarmType, startDate, endDate, isConfirmed);
	}
	@Override
	public Boolean sendAlarm(AlarmDto alarmDto) throws Exception {
		System.out.println(alarmDto);
		  //'1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',

		//1. userId로 fcmToken 가져오기
		Optional<User> ouser = userRepository.findById(alarmDto.getReceiverId());
		if(ouser.isEmpty()) {
			System.out.println("사용자 오류");
			return false;
		}
		String fcmToken = ouser.get().getFcmToken();
		if(fcmToken==null || fcmToken.trim().length()==0) {
			System.out.println("FCM Token 오류");
			return false;
		}
		//2. Alarm 저장
		Alarm alarm =  Alarm.builder().receiverId(alarmDto.getReceiverId())
			.alarmType(alarmDto.getAlarmType())
			.senderId(alarmDto.getSenderId())
			.senderNickname(alarmDto.getSenderNickname())
			.title(alarmDto.getTitle())
			.content(alarmDto.getContent())
			.confirm(false).build();
			alarmRepository.save(alarm);


		Message message = Message.builder()
				.setToken(fcmToken)
//				.setNotification(notification)
				.putData("alarmId", alarm.getAlarmId()+"")
				.putData("title", alarmDto.getTitle())
				.putData("content", alarmDto.getContent())
				.putData("sender", alarmDto.getSenderNickname())
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
