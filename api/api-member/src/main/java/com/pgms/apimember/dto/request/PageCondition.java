package com.pgms.apimember.dto.request;

import lombok.Getter;

@Getter
public class PageCondition {
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_SIZE = 10;

	private final Integer page;
	private final Integer size;

	public PageCondition(Integer page, Integer size) {
		this.page = isValidPage(page) ? page : DEFAULT_PAGE;
		this.size = isValidSize(size) ? size : DEFAULT_SIZE;
	}

	private Boolean isValidPage(Integer page) {
		return page != null && page > 0;
	}

	private Boolean isValidSize(Integer size) {
		return size != null && size > 0;
	}
}
