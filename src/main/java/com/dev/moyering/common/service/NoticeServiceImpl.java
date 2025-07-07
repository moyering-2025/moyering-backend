package com.dev.moyering.common.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.entity.AdminNotice;
import com.dev.moyering.common.repository.NoticeRepository;
import com.dev.moyering.util.PageInfo;

@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeRepository noticeRepository;
	@Override
	public List<AdminNoticeDto> findNoticesByPage(PageInfo pageInfo) throws Exception {

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		Long cnt = noticeRepository.countVisibleNotices();
		if(cnt == 0 || cnt == null) return null;
		Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		return noticeRepository.findNoticesByPage(pageRequest);
	}
	@Override
	public AdminNoticeDto findNoticeByNoticeId(Integer noticeId) throws Exception {
	    AdminNotice notice = noticeRepository.findById(noticeId)
	            .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
	    
	    return new AdminNoticeDto(
	        notice.getNoticeId(),
	        notice.getTitle(),
	        notice.getContent(),
	        notice.isPinYn(),
	        notice.isHidden(),
	        notice.getCreatedAt()
	    );
	}
	
}
