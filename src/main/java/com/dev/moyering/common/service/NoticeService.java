package com.dev.moyering.common.service;

import java.util.List;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.util.PageInfo;

public interface NoticeService {

	List<AdminNoticeDto> findNoticesByPage(PageInfo pageInfo) throws Exception;

	AdminNoticeDto findNoticeByNoticeId(Integer noticeId) throws Exception;

}
