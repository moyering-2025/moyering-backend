package com.dev.moyering.repository;

import com.dev.moyering.dto.admin.NoticeDto;
import com.dev.moyering.entity.admin.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeRepository extends JpaRepository<Notice, Integer>, AdminNoticeRepositoryCustom {
}
