package com.pgms.coredomain.domain.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventEdit(
	String title,
	String description,
	int runningTime,
	LocalDateTime startDate,
	LocalDateTime endDate,
	String viewRating,
	GenreType genreType,
	LocalDateTime bookingStartedAt,
	LocalDateTime bookingEndedAt,
	EventHall eventHall
) {
}
