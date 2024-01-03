package com.pgms.apievent.eventSearch.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;

import lombok.Getter;

@Getter
public class EventKeywordSearchRequest extends PageRequestDto {
	private String keyword;

	public EventKeywordSearchRequest(Integer page, Integer size, String keyword) {
		super(page, size);
		this.keyword = keyword;
	}

	public EventKeywordSearchDto toDto() {
		return EventKeywordSearchDto.builder()
			.keyword(keyword)
			.pageable(getPageable())
			.build();
	}
}
