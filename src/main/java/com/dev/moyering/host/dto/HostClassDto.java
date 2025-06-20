package com.dev.moyering.host.dto;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.HostClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostClassDto {

   private Integer classId;
   private Integer hostId;
   private Integer subCategoryId;
   private String name;
   private String locName;
   private String addr;
   private String detailAddr;
   private Double latitude;
   private Double longitude;
   private Integer recruitMin;
   private Integer recruitMax;
   private String imgName1;
   private String imgName2;
   private String imgName3;
   private String imgName4;
   private String imgName5;
   private String detailDescription;
   private String materialName;
   private String caution;
   private String incluision;
   private String preparation;
   private String keywords;
   private String portfolioName;
   private Integer price;
   //공통 필드 추가
   private Date startDate;
   private String status;
   private String category1;
   private String category2;
   
   private MultipartFile img1;
   private MultipartFile img2;
   private MultipartFile img3;
   private MultipartFile img4;
   private MultipartFile img5;
   
   private MultipartFile portfolio;
   private MultipartFile  material;
   
   public HostClass toEntity() {
      HostClass entity = HostClass.builder()
            .classId(classId)
            .name(name)
            .locName(locName)
            .addr(addr)
            .detailAddr(detailAddr)
            .latitude(latitude)
            .longitude(longitude)
            .recruitMin(recruitMin)
            .recruitMax(recruitMax)
            .img1(imgName1)
            .img2(imgName2)
            .img3(imgName3)
            .img4(imgName4)
            .img5(imgName5)
            .detailDescription(detailDescription)
            .material(materialName)
            .caution(caution)
            .incluision(incluision)
            .preparation(preparation)
            .keywords(keywords)
            .portfolio(portfolioName)
            .price(price)
            .build();
      if(hostId!=null) {
         entity.setHost(Host.builder()
               .hostId(hostId)
               .build());
      }
      if (subCategoryId!=null) {
    	  entity.setSubCategory(SubCategory.builder()
        		.subCategoryId(subCategoryId)
        		.build());
      }
      return entity;
   }
}
