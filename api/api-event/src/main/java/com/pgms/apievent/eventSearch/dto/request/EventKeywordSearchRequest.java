package com.pgms.apievent.eventSearch.dto.request;

import java.util.List;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;

import lombok.Getter;

@Getter
public class EventKeywordSearchRequest extends PageRequestDto {
	private String keyword;
	private List<String> genreType;
	private String startedAt;
	private String endedAt;

	public EventKeywordSearchRequest(
		Integer page,
		Integer size,
		String keyword,
		List<String> genreType,
		String startedAt,
		String endedAt
	) {
		super(page, size);
		this.keyword = keyword;
		this.genreType = genreType;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}

	public EventKeywordSearchDto toDto() {
		return EventKeywordSearchDto.builder()
			.keyword(keyword)
			.genreType(genreType)
			.startedAt(startedAt)
			.endedAt(endedAt)
			.pageable(getPageable())
			.build();
	}
}
