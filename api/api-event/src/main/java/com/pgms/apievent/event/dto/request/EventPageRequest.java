package com.pgms.apievent.event.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;

import lombok.Getter;

@Getter
public class EventPageRequest extends PageRequestDto {
	private String genreType;
	private Integer dateOffset;

	public EventPageRequest(Integer page, Integer size, String genreType, Integer dateOffset) {
		super(page, size);
		this.genreType = genreType;
		this.dateOffset = dateOffset;
	}
}
