package com.dev.moyering.host.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.HostDto;


public interface HostService {
	Integer registHost(HostDto hostDto,MultipartFile profile) throws Exception;

}
