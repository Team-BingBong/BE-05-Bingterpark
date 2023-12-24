package com.pgms.apievent.event.dto.request;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.GenreType;

public record EventUpdateRequest(
	String title,
	String description,
	int runningTime,
	LocalDateTime startDate,
	LocalDateTime endDate,
	String rating,
	GenreType genreType,
	String thumbnail,
	Long eventHallId
) {
}
