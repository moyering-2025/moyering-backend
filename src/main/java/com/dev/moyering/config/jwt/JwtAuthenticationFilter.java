package com.dev.moyering.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private HostRepository hostRepository;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, HostRepository hostRepository) {
		super(authenticationManager);
		this.hostRepository=hostRepository;
	}
	
	private JwtToken jwtToken = new JwtToken();

	//super의 attemptAuthentication 메소드가 실행되고 성공하면 successfulAuthentication가 호출된다.
	//attemptAuthentication 메소드가 리턴해준 Authentication을 파라미터로 받아옴
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		String username = principalDetails.getUsername();
		
		String accessToken = jwtToken.makeAccessToken(username);
		String refreshToken = jwtToken.makeRefreshToken(username);
		
		Map<String,String> map = new HashMap<>();
		map.put("access_token", JwtProperties.TOKEN_PREFIX+accessToken);
		map.put("refresh_token", JwtProperties.TOKEN_PREFIX+refreshToken);
		
		ObjectMapper objectMapper = new ObjectMapper(); //얘는 json과 string으로 서로 변환시켜줌
		String token = objectMapper.writeValueAsString(map);
		System.out.println(token);
		
		response.addHeader(JwtProperties.HEADER_STRING, token);
		response.setContentType("application/json; charset=utf-8");
		
		User user = principalDetails.getUser();
		
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("id", user.getUserId());
		userInfo.put("username", user.getUsername());
		userInfo.put("name", user.getName());
		userInfo.put("email", user.getEmail());
		userInfo.put("userType", user.getUserType());
		if(user.getUserType().equals("ROLE_HT")) {
			Optional<Host> ohost = hostRepository.findByUserId(user.getUserId());
			if(ohost.isPresent()) {
				userInfo.put("hostId", ohost.get().getHostId());
			}
		}
		response.getWriter().write(objectMapper.writeValueAsString(userInfo));
	}
}
