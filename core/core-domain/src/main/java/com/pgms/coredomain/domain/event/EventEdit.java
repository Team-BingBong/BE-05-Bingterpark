package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record EventEdit(
	String title,
	String description,
	int runningTime,
	LocalDateTime startDate,
	LocalDateTime endDate,
	String rating,
	GenreType genreType,
	String thumbnail,
	EventHall eventHall
) {
}
