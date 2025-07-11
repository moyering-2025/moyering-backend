package com.dev.moyering.socialing.repository;

public interface DmMessageRepositoryCustom {
    long markMessagesAsRead(Integer roomId, Integer userId);
}
