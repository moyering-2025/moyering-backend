package com.dev.moyering.config.oauth;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
	String getProfileImage();
	String getNickName();//nickname. 카카오에서는 profileName으로 줌
}
