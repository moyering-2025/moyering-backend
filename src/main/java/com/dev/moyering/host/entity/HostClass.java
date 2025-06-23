package com.dev.moyering.host.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.host.dto.HostClassDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class HostClass {

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   private Integer classId;
   @ManyToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="hostId")
   private Host host;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "subCategoryId")
   private SubCategory subCategory;
   @Column
   private String name;
   @Column
   private String locName;
   @Column
   private String addr;
   @Column
   private String detailAddr;
   @Column
   private Double latitude;
   @Column
   private Double longitude;
   @Column
   private Integer recruitMin;
   @Column
   private Integer recruitMax;
   @Column
   private String img1;
   @Column
   private String img2;
   @Column
   private String img3;
   @Column
   private String img4;
   @Column
   private String img5;
   @Column
   private String detailDescription;
   @Column
   private String material;
   @Column
   private String caution;
   @Column
   private String incluision;
   @Column
   private String preparation;
   @Column
   private String keywords;
   @Column
   private String portfolio;
   @Column
   private Integer price;

   @Column(name = "reg_date")
   private Date regDate; // 강의 개설일
   
   
   public HostClassDto toDto() {
      HostClassDto dto = HostClassDto.builder()
            .classId(classId)
            .name(name)
            .locName(locName)
            .addr(addr)
            .detailAddr(detailAddr)
            .latitude(latitude)
            .longitude(longitude)
            .recruitMin(recruitMin)
            .recruitMax(recruitMax)
            .imgName1(img1)
            .imgName2(img2)
            .imgName3(img3)
            .imgName4(img4)
            .imgName5(img5)
            .detailDescription(detailDescription)
            .materialName(material)
            .caution(caution)
            .incluision(incluision)
            .preparation(preparation)
            .keywords(keywords)
            .portfolioName(portfolio)
            .price(price)
            .regDate(regDate)
            .build();
      if(host!=null) {
         dto.setHostId(host.getHostId());
      }
      if (subCategory!=null) {
    	  dto.setSubCategoryId(subCategory.getSubCategoryId());
    	  dto.setCategory2(subCategory.getSubCategoryName());
    	  dto.setCategory1(subCategory.getFirstCategory().getCategoryName());
      }
      return dto;
   }

}