package com.pgms.apievent.event.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class EventPageRequest extends PageRequestDto {
	private String genreType;
	@Min(1) @Max(10000)
	private Integer dateOffset;

	public EventPageRequest(Integer page, Integer size, String genreType, Integer dateOffset) {
		super(page, size);
		this.genreType = genreType;
		this.dateOffset = dateOffset;
	}
}
