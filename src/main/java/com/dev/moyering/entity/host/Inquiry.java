package com.dev.moyering.entity.host;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.dev.moyering.dto.host.InquiryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Inquiry {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer InquiryId;
	@OneToOne
	@JoinColumn(name="studentId")
	private ClassRegist student;
	@Column
	private String className;
	@Column
	private String studentName;
	@Column
	private Date inquiryDate;
	@Column
	private String state;
	@Column
	private String iqResContent;
	@Column
	private Date ResponseDate;
	@ManyToOne
	@JoinColumn(name="calendarId")
	private ClassCalendar classCalendar;
	@OneToOne
	@JoinColumn(name="hostId")
	private Host host;
	@Column
	private String content;
	
	
}
