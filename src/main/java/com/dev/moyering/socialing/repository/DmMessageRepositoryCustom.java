package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.DmRoomDto;

import java.util.List;

public interface DmMessageRepositoryCustom {

    long markMessagesAsRead(Integer roomId, Integer userId);


}
