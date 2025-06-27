package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Integer>, FeedRepositoryCustom {

    Optional<Feed> findByFeedIdAndIsDeletedFalse(Integer feedId);
    List<Feed> findAllByUserUserIdOrderByCreateDateDesc(Integer userId);

    List<Feed> findByUserNickName(String nickname);
}
