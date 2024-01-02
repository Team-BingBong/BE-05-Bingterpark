package com.pgms.apievent.eventSearch.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coreinfraes.dto.EventSearchDto;

import lombok.Getter;

@Getter
public class EventSearchRequest extends PageRequestDto {
	private String title;
	private GenreType genreType;

	public EventSearchRequest(int page, int size, String title, GenreType genreType) {
		super(page, size);
		this.title = title;
		this.genreType = genreType;
	}

	public EventSearchDto toDto() {
		return EventSearchDto.builder()
			.page(page)
			.size(size)
			.title(title)
			.genreType(genreType)
			.pageable(getPageable())
			.build();
	}
}
