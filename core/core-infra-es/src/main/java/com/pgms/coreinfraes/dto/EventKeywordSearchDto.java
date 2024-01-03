package com.pgms.coreinfraes.dto;

import org.springframework.data.domain.Pageable;

import lombok.Builder;

@Builder
public record EventKeywordSearchDto(
	String keyword,
	Pageable pageable
) {

}
