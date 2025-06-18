package com.dev.moyering.config.jwt;

public interface JwtProperties {
	String SECRET = "코스타"; // 절대 노출되서는 안됌. 노출될시 토큰 위조가 가능해짐
//	Integer ACCESS_EXPIRATION_TIME = 60000*60*2;
	Integer ACCESS_EXPIRATION_TIME = 1000; // 밀리 세컨드여서 1초인데 일반적으로는 5~15분으로 설정함.
	Integer REFRESH_EXPIRATION_TIME = 60000*60*24;
	String TOKEN_PREFIX = "Bearer ";//Bearer을 써주는 이유는 개발자가 필요해서 하는게 아니라. 통신 규약을
													//맞추기 위해서 넣어주는 것임. 일종의 규칙(Bearer토큰 기반으로 할경우)
	String HEADER_STRING = "Authorization";
}
