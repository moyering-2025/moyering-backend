package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.entity.AdminNotice;
import com.dev.moyering.admin.repository.AdminNoticeRepository;
import com.dev.moyering.admin.repository.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j

    public class AdminNoticeServiceImpl implements AdminNoticeService {
    private final AdminNoticeRepository adminNoticeRepository;

    /*** 공지사항 등록*/
    @Override
    @Transactional
    public AdminNoticeDto createNotice(AdminNoticeDto noticeDto) {
        try {
            AdminNotice adminNotice = noticeDto.toEntity();
            AdminNotice savedAdminNotice = adminNoticeRepository.save(adminNotice);
            AdminNoticeDto result = savedAdminNotice.toDto();
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /*** 공지사항 수정*/
    @Override
    @Transactional
    public AdminNoticeDto updateNotice(Integer noticeId, AdminNoticeDto noticeDto) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID : " + noticeId));
        // 비즈니스 메서드 호출해서 값 수정
        adminNotice.changeNotice(noticeDto.getTitle(), noticeDto.getContent());
        adminNotice.changePinStatus(noticeDto.isPinYn());
        return adminNotice.toDto();
    }

    /*** 공지사항 삭제*/
    @Override
    @Transactional
    public void deleteNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
        adminNoticeRepository.delete(adminNotice);
    }


    /*** 공지사항 목록 조회 (검색 포함)*/
    @Override
    public Page<AdminNoticeDto> getNoticeList(String searchKeyword, Pageable pageable) {
        try {
            Page<AdminNoticeDto> result;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                result = adminNoticeRepository.findNoticesByKeyword(searchKeyword, pageable);
            } else {
                Page<AdminNotice> noticePage = adminNoticeRepository.findAll(pageable);
                result = noticePage.map(AdminNotice::toDto); // Entity -> DTO 변환
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /*** 공지사항 단건 조회*/
    @Override
    public AdminNoticeDto getNoticeById(Integer noticeId) {
        AdminNoticeDto notice = adminNoticeRepository.findNoticeByNoticeId(noticeId);
        if (notice == null) {
            throw new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId);
        }
        return notice;
    }

    /*** 핀 상태 변경*/
    @Override
    @Transactional
    public AdminNoticeDto changePinStatus(Integer noticeId, boolean pinYn) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId));
        adminNotice.changePinStatus(pinYn);
        return adminNotice.toDto();
    }


    // 보이기, 숨기기 처리 상태
    @Override
    @Transactional
    public void hideNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        adminNotice.hide(); // isHidden = true
        adminNoticeRepository.save(adminNotice);
    }

    @Override
    @Transactional
    public void showNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        adminNotice.show(); // isHidden = false
        adminNoticeRepository.save(adminNotice);
    }

	@Override
	public List<AdminNotice> selectAllNotice() throws Exception {
		List<AdminNotice> list = adminNoticeRepository.findAll();
		return list;
	}

}
