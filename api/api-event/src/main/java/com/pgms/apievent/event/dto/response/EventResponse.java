package com.pgms.apievent.event.dto.response;

import com.pgms.coredomain.domain.event.Event;

public record EventResponse(
	Long id,
	String title,
	String description,
	int runningTime,
	String startedAt,
	String endedAt,
	String rating,
	String genreType,
	String thumbnail,
	String bookingStartedAt,
	String bookingEndedAt,
	String eventHall,
	Double averageScore
) {
	public static EventResponse of(Event event) {
		return new EventResponse(
			event.getId(),
			event.getTitle(),
			event.getDescription(),
			event.getRunningTime(),
			event.getStartedAt().toString(),
			event.getEndedAt().toString(),
			event.getViewRating(),
			event.getGenreType().getDescription(),
			event.getThumbnail(),
			event.getBookingStartedAt().toString(),
			event.getBookingEndedAt().toString(),
			event.getEventHall().getName(),
			event.getAverageScore()
		);
	}
}
