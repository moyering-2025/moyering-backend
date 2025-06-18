package com.dev.moyering.config.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo=null;
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		 if(registrationId.equals("kakao")) {
			oAuth2UserInfo = new KaKaoUserInfo(oAuth2User.getAttribute("properties"));
		}else {
			System.out.println("카카오와 네이버만 지원");
		}
		
		//1. DB에서 조회
		Optional<User> ouser =  userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		User user = null;
		if(ouser.isEmpty()) { //1-1. 가입되어 있지 않으면 삽입
			user = User.builder()
						.username(oAuth2UserInfo.getProviderId())
						.userType("ROLE USER")
						.provider(oAuth2UserInfo.getProvider())
						.providerId(oAuth2UserInfo.getProviderId())
						.build();
		userRepository.save(user);
		}else { //1-2.가입되어 있으면 업데이트
			user = ouser.get();
			user.setEmail(oAuth2UserInfo.getEmail());
			userRepository.save(user);
		}
		return new PrincipalDetails(user,oAuth2User.getAttributes());
	}

}
