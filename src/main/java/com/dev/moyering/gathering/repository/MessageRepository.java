package com.dev.moyering.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.entity.Message;


public interface MessageRepository  extends JpaRepository<Message, Integer>, MessageRepositoryCustom {

}
