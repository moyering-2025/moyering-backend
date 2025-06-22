package com.dev.moyering.host.service;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.entity.Host;


public interface HostService {
	Integer registHost(HostDto hostDto,MultipartFile profile) throws Exception;
	Host findByUserId(Integer userId) throws Exception;
}
