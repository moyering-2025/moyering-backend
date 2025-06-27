package com.dev.moyering.user.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.moyering.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>,  UserRepositoryCustom {
	Optional<User> findByUsername(String username);
	Optional<User> findByProviderAndProviderId(String provider,String providerId);



	//feed
	Optional<User> findByNickName(String nickName);
	
	@Transactional
	@Modifying
	@Query("update User u set u.fcmToken=:fcmToken where u.userId=:userId")
	void updateFcmToken(@Param("userId") Integer userId, @Param("fcmToken") String fcmToken);
}
