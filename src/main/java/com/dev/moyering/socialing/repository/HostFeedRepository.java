package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.HostFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostFeedRepository extends JpaRepository<HostFeed, Integer>,HostFeedRepositoryCustom {

}
