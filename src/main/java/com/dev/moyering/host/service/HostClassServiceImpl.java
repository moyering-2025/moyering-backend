package com.dev.moyering.host.service;

import java.io.File;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.common.repository.SubCategoryRepository;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HostClassServiceImpl implements HostClassService {
    private final HostClassRepository hostClassRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final UserRepository userRepository;
    private final HostRepository hostRepository;
    private final SubCategoryRepository subCategoryRepository;
    
    
	@Value("${iupload.path}")
	private String iuploadPath;


	@Override
	public List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception {
	    List<HostClass> result;
		if (userId == null) {
	        result = hostClassRepository.findRecommendClassesForUser(null);
	    } else {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
	        result = hostClassRepository.findRecommendClassesForUser(user);
	    }
		System.out.println(result+"rrr");
		
		
		List<Integer> classIds = result.stream()
		        .map(HostClass::getClassId)
		        .collect(Collectors.toList());

	    Map<Integer, Date> startDateMap = classCalendarRepository.findEarliestStartDatesByClassIds(classIds);

        return result.stream()
        		.map(hostClass -> {
                    HostClassDto dto = hostClass.toDto();
                    dto.setStartDate(startDateMap.get(hostClass.getClassId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }


	@Override
	public Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception {
		MultipartFile[] files = {hostClassDto.getImg1(),hostClassDto.getImg2(),hostClassDto.getImg3(),hostClassDto.getImg4(),hostClassDto.getImg5()
				,hostClassDto.getMaterial(), hostClassDto.getPortfolio()};
		for(MultipartFile file : files) {
			if(file!=null && !file.isEmpty()) {
				File upFile = new File(iuploadPath, file.getOriginalFilename());
				file.transferTo(upFile);
			}			
		}
		if(files[0]!=null && !files[0].isEmpty()) hostClassDto.setImgName1(files[0].getOriginalFilename());
		if(files[1]!=null && !files[1].isEmpty()) hostClassDto.setImgName2(files[1].getOriginalFilename());
		if(files[2]!=null && !files[2].isEmpty()) hostClassDto.setImgName3(files[2].getOriginalFilename());
		if(files[3]!=null && !files[3].isEmpty()) hostClassDto.setImgName4(files[3].getOriginalFilename());
		if(files[4]!=null && !files[4].isEmpty()) hostClassDto.setImgName5(files[4].getOriginalFilename());
		if(files[5]!=null && !files[5].isEmpty()) hostClassDto.setMaterialName(files[5].getOriginalFilename());
		if(files[6]!=null && !files[6].isEmpty()) hostClassDto.setPortfolioName(files[6].getOriginalFilename());
		HostClass hostClass = hostClassDto.toEntity(); 
		hostClassRepository.save(hostClass);
		
		dates.forEach(date-> { 
			ClassCalendar cc = ClassCalendar.builder()
											.startDate(date)
											.endDate(date)
											.status("검수중")
											.hostClass(HostClass.builder()
																.classId(hostClass.getClassId())
																.build())
											.build();
			
			classCalendarRepository.save(cc);
		});
		
		return hostClass.getClassId();
	}
}
