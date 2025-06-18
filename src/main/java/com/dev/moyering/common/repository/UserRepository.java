package com.dev.moyering.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom{

}
