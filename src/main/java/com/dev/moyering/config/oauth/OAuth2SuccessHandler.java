package com.dev.moyering.config.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.config.jwt.JwtProperties;
import com.dev.moyering.config.jwt.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private JwtToken jwtToken = new JwtToken();
	private static final String URI = "http://localhost:5173/token";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		String accessToken = jwtToken.makeAccessToken(principalDetails.getUsername());
		String refreshToken = jwtToken.makeRefreshToken(principalDetails.getUsername());
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,String> map = new HashMap<>();
		map.put("access_token",JwtProperties.TOKEN_PREFIX+accessToken);
		map.put("refresh_token",JwtProperties.TOKEN_PREFIX+refreshToken);
		
		String token = objectMapper.writeValueAsString(map);
		System.out.println(token);
		
		String targetUrl = UriComponentsBuilder.fromUriString(URI)
				.queryParam("token",token)
				.build().toString();
		
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
	
}
