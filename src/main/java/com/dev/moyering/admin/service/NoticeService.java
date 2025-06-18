package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    Page<NoticeDto> getNoticeList(String searchKeyword, Pageable pageable) throws Exception; // 리스트 조회

    NoticeDto getNoticeById(Integer noticeId) throws Exception; // 단건 조회 (모달)

    NoticeDto createNotice(NoticeDto noticeDto) throws Exception;     // 등록 + 검증 + 변환

    NoticeDto updateNotice(Integer noticeId, NoticeDto dto) throws Exception; // 수정 + 검증 + 권한체크

    void deleteNotice(Integer noticeId) throws Exception;             // 삭제 + 권한체크 + 로깅

    NoticeDto changePinStatus(Integer noticeId, boolean pin) throws Exception; // 비즈니스 로직

    void noticeVisibility(Integer noticeId) throws Exception;

    void hideNotice(Integer noticeId) throws Exception;

    void showNotice(Integer noticeId) throws Exception;

}