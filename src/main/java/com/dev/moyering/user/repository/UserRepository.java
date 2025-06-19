package com.dev.moyering.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>,  UserRepositoryCustom {
	Optional<User> findByUsername(String username);
	Optional<User> findByProviderAndProviderId(String provider,String providerId);
}
