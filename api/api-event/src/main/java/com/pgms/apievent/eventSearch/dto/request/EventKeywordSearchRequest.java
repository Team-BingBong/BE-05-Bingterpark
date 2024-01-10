package com.pgms.apievent.eventSearch.dto.request;

import com.pgms.apievent.common.dto.request.PageRequestDto;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import java.util.List;

@Getter
public class EventKeywordSearchRequest extends PageRequestDto {
	@Size(min= 2,message = "검색어를 2글자 이상 입력해주세요.")
	private String keyword;
	private List<String> genreType;
	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private String startedAt;
	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
