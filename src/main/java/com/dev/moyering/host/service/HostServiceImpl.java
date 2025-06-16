package com.dev.moyering.host.service;

import java.io.File;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.repository.HostRepository;
@Service
public class HostServiceImpl implements HostService {
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	private HostRepository hostRepository;
	
	@Value("${iupload.path}")
	private String iuploadPath;

	@Override
	public Integer registHost(HostDto hostDto, MultipartFile profile) throws Exception {
		if(profile!=null && !profile.isEmpty()) {
			hostDto.setProfile(profile.getOriginalFilename());
			File upFile = new File(iuploadPath,hostDto.getProfile());
			profile.transferTo(upFile);
		}
		Host host = hostDto.toEntity();
		hostRepository.save(host);
		Integer hostId = host.getHostId();
		entityManager.clear();
		return hostId;
	}

}
