package com.pgms.apievent.event.dto.response;

import com.pgms.coredomain.domain.event.Event;

public record EventResponse(
	Long id,
	String title,
	String description,
	int runningTime,
	String startDate,
	String endDate,
	String rating,
	String genreType,
	String thumbnail,
	String eventHall,
	Double averageScore
) {
	public static EventResponse of(Event event) {
		return new EventResponse(
			event.getId(),
			event.getTitle(),
			event.getDescription(),
			event.getRunningTime(),
			event.getStartDate().toString(),
			event.getEndDate().toString(),
			event.getRating(),
			event.getGenreType().getDescription(),
			event.getThumbnail(),
			event.getEventHall().getName(),
			event.getAverageScore()
		);
	}
}
