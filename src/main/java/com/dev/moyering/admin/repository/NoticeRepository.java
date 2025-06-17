package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>, NoticeRepositoryCustom {
}
