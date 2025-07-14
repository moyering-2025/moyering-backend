package com.dev.moyering.socialing.repository;


import com.dev.moyering.socialing.entity.QDmMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class DmMessageRepositoryImpl implements DmMessageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public long markMessagesAsRead(Integer roomId, Integer userId) {
        QDmMessage dm = QDmMessage.dmMessage;

        return queryFactory
                .update(dm)
                .set(dm.isRead, true)
                .where(dm.dmRoom.roomId.eq(roomId)
                        .and(dm.senderId.userId.ne(userId))
                        .and(dm.isRead.eq(false)))
                .execute();
    }
}
