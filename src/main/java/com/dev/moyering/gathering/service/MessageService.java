package com.dev.moyering.gathering.service;

public interface MessageService {

	Boolean sendMessage(Integer gatheringId, Integer senderId, String messageContent) throws Exception;

}
