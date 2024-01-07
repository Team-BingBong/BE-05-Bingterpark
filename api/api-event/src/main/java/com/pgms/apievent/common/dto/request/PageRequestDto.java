package com.pgms.apievent.common.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
public abstract class PageRequestDto {

	@Min(value = 1, message = "1 이상의 올바른 페이지 번호를 입력해주세요.")
	protected Integer page;

	@Min(value = 1, message = "1 이상의 올바른 페이지 당 글 개수를 입력해주세요.")
	@Max(value = 100, message = "100 이하의 올바른 페이지 당 글 개수를 입력해주세요.")
	protected Integer size;

	public Pageable getPageable() {
		return PageRequest.of(page - 1, size);
	}

	public PageRequestDto(Integer page, Integer size) {
		this.page = page;
		this.size = size;
		if (page == null) {
			this.page = 1;
		}
		if (size == null) {
			this.size = 10;
		}
	}
}
