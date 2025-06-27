package com.dev.moyering.host.dto;

import java.util.List;

import com.dev.moyering.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostPageResponseDto<T> {
	private List<T> content;
	private PageInfo pageInfo; 

}
