package com.pgms.apievent.common.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponseDto<T>(
	int currentPage,
	int pageSize,
	long totalElements,
	int startIndex,
	int endIndex,
	boolean hasPreviousPage,
	boolean hasNextPage,
	List<T> data
) {
	public static <T> PageResponseDto<T> of(Page<T> page) {
		int currentPage = page.getNumber() + 1;
		int pageSize = page.getSize();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();

		int startIndex = (currentPage - 1) * pageSize + 1;
		int endIndex = Math.min(currentPage * pageSize, (int)totalElements);

		boolean hasPreviousPage = currentPage > 1;
		boolean hasNextPage = currentPage < totalPages;

		return new PageResponseDto<>(
			currentPage,
			pageSize,
			totalElements,
			startIndex,
			endIndex,
			hasPreviousPage,
			hasNextPage,
			page.getContent()
		);
	}
}
