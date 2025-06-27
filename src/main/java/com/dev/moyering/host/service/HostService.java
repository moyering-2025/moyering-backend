package com.dev.moyering.host.service;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.user.entity.User;


public interface HostService {
	Integer registHost(HostDto hostDto,MultipartFile profile) throws Exception;
	Host findByUserId(Integer userId) throws Exception;
	Host findByHostId(Integer hostId) throws Exception;
	Host updateHost(Integer hostId,String name,String publicTel,String email,String intro,MultipartFile profile) throws Exception;
	Host updateHostSettlement(Integer hostId,String bankName,String accName,String accNum,MultipartFile idCard) throws Exception;
	HostDto getHostById(Integer hostId) throws Exception;
	
}
