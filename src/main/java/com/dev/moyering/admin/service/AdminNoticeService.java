package com.dev.moyering.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.entity.AdminNotice;

public interface AdminNoticeService {
    Page<AdminNoticeDto> getNoticeList(String searchKeyword, Pageable pageable) throws Exception; // 리스트 조회

    AdminNoticeDto getNoticeById(Integer noticeId) throws Exception; // 단건 조회 (모달)

    AdminNoticeDto createNotice(AdminNoticeDto noticeDto) throws Exception;     // 등록 + 검증 + 변환

    AdminNoticeDto updateNotice(Integer noticeId, AdminNoticeDto dto) throws Exception; // 수정 + 검증 + 권한체크

    void deleteNotice(Integer noticeId) throws Exception;             // 삭제 + 권한체크 + 로깅

    AdminNoticeDto changePinStatus(Integer noticeId, boolean pin) throws Exception; // 비즈니스 로직

    void hideNotice(Integer noticeId) throws Exception;

    void showNotice(Integer noticeId) throws Exception;
    
    List<AdminNotice> selectAllNotice() throws Exception;

}