package com.dev.moyering.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendVerificationEmail(String toEmail,String token) {
		String subject = "이메일 인증";
		String text = "다음 인증코드를 입력하여 이메일 인증을 완료하세요!:\n" + 
		"인증코드 : "+token;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(text);
		
		javaMailSender.send(message);
	}
}
