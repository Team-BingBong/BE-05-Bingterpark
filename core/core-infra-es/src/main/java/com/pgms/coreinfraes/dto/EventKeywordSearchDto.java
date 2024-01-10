package com.pgms.coreinfraes.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;

@Builder
public record EventKeywordSearchDto(
	String keyword,
	List<String> genreType,
	String startedAt,
	String endedAt,
	Pageable pageable
) {

}
