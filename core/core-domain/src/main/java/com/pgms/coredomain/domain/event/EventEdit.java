package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record EventEdit(
	String title,
	String description,
	int runningTime,
	LocalDateTime startDate,
	LocalDateTime endDate,
	String rating,
	GenreType genreType,
	EventHall eventHall
) {
}
