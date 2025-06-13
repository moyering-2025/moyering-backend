package com.dev.moyering.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.dto.user.GatheringDto;
import com.dev.moyering.repository.GatheringRepository;
import com.dev.moyering.util.PageInfo;

@Service
public class GatheringServiceImpl implements GatheringService {
	@Value("${iupload.path}")
	private String iuploadPath;

	@Value("${dupload.path}")
	private String duploadPath;
	@Autowired
	public GatheringRepository gatheringRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public Integer writeGathering(GatheringDto gatheringDto, MultipartFile ifile) throws Exception {
		return null;
		// 게더링 등록
	}
	@Override
	public void modifyGathering(GatheringDto gatheringDto, MultipartFile ifile) throws Exception {
		// 게더링 수정
		
	}
	
	@Override
	public List<GatheringDto> myGatheringList(Integer userId, PageInfo pageInfo, String word) throws Exception {
		// 내가 등록한 게더링 목록 + 페이지네이션, 제목으로 검색 
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		
		return null;
	}
	@Override
	public Boolean getGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<GatheringDto> myGatheringApplyList(Integer userId, PageInfo pageInfo, String word) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GatheringDto detailGathering(Integer gatheringId) throws Exception {
		// 게더링 조회
		return gatheringRepository.selectGathering(gatheringId).toDto();
	}	
}
