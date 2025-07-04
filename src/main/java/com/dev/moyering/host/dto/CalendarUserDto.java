package com.dev.moyering.host.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarUserDto {

	private Integer calendarId;
	private String name;
	private Date startDate;
	
}
