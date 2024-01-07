package com.pgms.coreinfraes.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public record EventKeywordSearchDto(
	String keyword,
	List<String> genreType,
	String startedAt,
	String endedAt,
	Pageable pageable
) {

}
