package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.DmMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DmMessageRepository extends JpaRepository<DmMessage, Long>, DmMessageRepositoryCustom {

    @Modifying
    @Transactional
    @Query("UPDATE DmMessage dm SET dm.isRead = true " +
            "WHERE dm.dmRoom.roomId = :roomId " +
            "AND dm.senderId.userId != :userId " +
            "AND dm.isRead = false")
    int markMessagesAsReadJpa(@Param("roomId") Integer roomId, @Param("userId") Integer userId);
}
