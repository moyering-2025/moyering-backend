package com.dev.moyering.config.oauth;

import java.util.Map;

public class KaKaoUserInfo implements OAuth2UserInfo {

	
	private Map<String,Object> attributes;
	private Map<String,Object> properties;
	private Map<String,Object> kakaoAccount;
	private Map<String,Object> profile;
	public KaKaoUserInfo(Map<String,Object> attributes) {
		System.out.println(attributes);
		this.attributes=attributes;
		properties = (Map<String, Object>) attributes.get("properties");
		kakaoAccount = (Map<String,Object>) attributes.get("kakao_account");
		profile = (Map<String, Object>) kakaoAccount.get("profile");
	}
	
	@Override
    public String getProviderId() {
		return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
    	return (String)properties.get("nickname");
    }

	@Override
	public String getProfileImage() {
		return (String)properties.get("profile_image");
	}

	@Override
	public String getNickName() {
		return (String) profile.get("nickname");
	}

}
