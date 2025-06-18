package com.dev.moyering.host.dto;

import java.sql.Date;

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
   private double latitude;
   private double longitude;
   private Integer recruitMin;
   private Integer recruitMax;
   private String img1;
   private String img2;
   private String img3;
   private String img4;
   private String img5;
   private String detailDescription;
   private String material;
   private String caution;
   private String incluision;
   private String preparation;
   private String keywords;
   private String portfolio;
   private Integer price;
   //공통 필드 추가
   private Date startDate;
   private String category1;
   private String category2;
   
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
            .img1(img1)
            .img2(img2)
            .img3(img3)
            .img4(img4)
            .img5(img5)
            .detailDescription(detailDescription)
            .material(material)
            .caution(caution)
            .incluision(incluision)
            .preparation(preparation)
            .keywords(keywords)
            .portfolio(portfolio)
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
