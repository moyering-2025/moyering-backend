package com.dev.moyering.common.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.admin.dto.AdminNoticeDto;

public interface NoticeRepositoryCustom {

	long countVisibleNotices() throws Exception;

	List<AdminNoticeDto> findNoticesByPage(PageRequest pageRequest) throws Exception;


}
