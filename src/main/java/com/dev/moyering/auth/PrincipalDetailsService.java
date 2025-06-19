package com.dev.moyering.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

// security 설정에서 loginProcessingUrl("/loginProc")
// /loginProc 요청이 오면 자동으로 UserDetailsService의 타입으로 IoC 되어있는 loadUserByUsername 함수가 호출된다
@Service
public class PrincipalDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository; 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
		
		return new PrincipalDetails(user);//이 반환은 Spring Security에서 원하기 때문에 해주는것. 
														//개발자가 필요해서 하는게 x
	}
}
