package com.dev.moyering.host.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

	@Override
	public Host findByUserId(Integer userId) throws Exception {
		Host host = hostRepository.findByUserId(userId).get();
		return host;
	}

	@Override
	public Host findByHostId(Integer hostId) throws Exception {
		return hostRepository.findById(hostId).get();
	}

	@Override
	public Host updateHost(Integer hostId,String name, String publicTel, String email, String intro, MultipartFile profile) throws Exception {
		Host host = hostRepository.findById(hostId).get();
		if(host != null) {
			if(name!= null) host.setName(name);
			if(publicTel != null) host.setPublicTel(publicTel);
			if(email != null) host.setEmail(email);
			if(intro != null) host.setIntro(intro);
			if(profile != null)host.setProfile(profile.getOriginalFilename());			
		}
		if(profile != null && !profile.isEmpty()) {
			String fileName = profile.getOriginalFilename();
		    Path savePath = Paths.get(iuploadPath, fileName);  // ✅ 폴더 + 파일명
		    Files.write(savePath, profile.getBytes());
		    host.setIdCard(fileName);  // DB에는 파일명만 저장
		}
		hostRepository.save(host);
		Host sHost = hostRepository.findById(hostId).get();
		return sHost;
	}

	@Override
	public Host updateHostSettlement(Integer hostId, String bankName, String accName, String accNum, MultipartFile idCard)
			throws Exception {
		System.out.println(hostId);
		Host host = hostRepository.findById(hostId).get();
		System.out.println(host);
		if(host != null) {
			if(bankName!=null)host.setBankName(bankName);
			if(accName!=null)host.setAccName(accName);
			if(accNum!=null)host.setAccNum(accNum);
			if(idCard!=null)host.setIdCard(idCard.getOriginalFilename());
		}
		if (idCard != null && !idCard.isEmpty()) {
		    String fileName = idCard.getOriginalFilename();
		    Path savePath = Paths.get(iuploadPath, fileName);  // ✅ 폴더 + 파일명
		    Files.write(savePath, idCard.getBytes());
		    host.setIdCard(fileName);  // DB에는 파일명만 저장
		}
		hostRepository.save(host);
		Host sHost = hostRepository.findById(hostId).get();
		return sHost;
	}

}
