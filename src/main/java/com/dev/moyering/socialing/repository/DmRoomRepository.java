package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.DmRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DmRoomRepository extends JpaRepository<DmRoom, Integer> {

    // 같은 두 유저의 방이 이미 있는지 확인
    Optional<DmRoom> findByUser1UserIdAndUser2UserId(Integer user1Id, Integer user2Id);

    // 양방향 (user1 <-> user2) 확인하기 위해 추가
    Optional<DmRoom> findByUser1UserIdAndUser2UserIdOrUser1UserIdAndUser2UserId(
            Integer user1Id1, Integer user2Id1,
            Integer user1Id2, Integer user2Id2);

    @Query("select r from DmRoom r where r.user1.userId = :userId or r.user2.userId = :userId")
    List<DmRoom> findAllByUser(@Param("userId") Integer userId);


}
