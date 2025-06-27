package com.dev.moyering.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.dev.moyering.config.jwt.JwtAuthenticationFilter;
import com.dev.moyering.config.jwt.JwtAuthorizationFilter;
import com.dev.moyering.config.oauth.OAuth2SuccessHandler;
import com.dev.moyering.config.oauth.PrincipalOAuth2UserService;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.repository.UserRepository;

@Configuration // IoC 빈(bean) 등록
@EnableWebSecurity //필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private CorsFilter corsFilter;
	
	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler; 
	
	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HostRepository hostRepository;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	//Authentication : 인증, 유저 확인(로그인)
	//Authorization : 인가, 유저의 권한
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{
		http.addFilter(corsFilter) //다른 도메인 접근 허용
			.csrf().disable() //csrf 공격 비활성화
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //session비활성화
		
		http.formLogin().disable()  //로그인 폼 비활성화
			.httpBasic().disable() //httpBasic은 header에 username,password를 암호화하지 않은 상태로 주고받는다. 이를 사용하지 않겠다는 것.
			.addFilterAt(new JwtAuthenticationFilter(authenticationManager,hostRepository,userRepository), UsernamePasswordAuthenticationFilter.class);
			
		http.oauth2Login()
			.authorizationEndpoint().baseUri("/oauth2/authorization") //front로그인 uri
			.and()
			.redirectionEndpoint().baseUri("/callback/*")
			.and().userInfoEndpoint().userService(principalOAuth2UserService)
			.and().successHandler(oAuth2SuccessHandler);
		
		http.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
			.authorizeRequests()
			.antMatchers("/user/**").authenticated() //로그인 필수
			.antMatchers("/host/regist").authenticated()
			.antMatchers("/host/**").access("hasRole('ROLE_HT') or hasRole('ROLE_MG')") //로그인 필수
//			.antMatchers("/api/login/","/api/verify").permitAll()
			.antMatchers("/admin/**").access("hasRole('ROLE_MG')")//로그인 필수 && 권한이 ADMIN이거나 MANAGER 만 허용 
			.anyRequest().permitAll();
		
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder encoderPassword() {
		return new BCryptPasswordEncoder();
	}

}
