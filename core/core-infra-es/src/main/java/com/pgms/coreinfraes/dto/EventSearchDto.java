package com.pgms.coreinfraes.dto;

import org.springframework.data.domain.Pageable;

import com.pgms.coredomain.domain.event.GenreType;

import lombok.Builder;

@Builder
public record EventSearchDto(
	String title,
	GenreType genreType,
	Pageable pageable
) {

}
