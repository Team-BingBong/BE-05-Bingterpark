package com.pgms.apievent.event.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPageRequest extends PageRequestDto {
	private String genreType;
	private String dateRange;

	public EventPageRequest() {
		super(null, null);
		this.genreType = "CONCERT";
		this.dateRange = "DAILY";
	}

	public EventPageRequest(Integer page, Integer size, String genreType, String dateRange) {
		super(page, size);
		this.genreType = genreType;
		this.dateRange = dateRange;
	}
}
