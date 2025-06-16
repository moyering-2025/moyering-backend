package com.dev.moyering.host.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.repository.HostClassRepository;

@Service
public class HostClassServiceImpl implements HostClassService {

	@Autowired
	private HostClassRepository hostClassRepository;

	@Override
	public void registClass(HostClassDto hostClassDto) throws Exception {
		hostClassRepository.save(hostClassDto.toEntity());
	}
	
	
}
