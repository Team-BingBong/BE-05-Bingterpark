package com.pgms.apievent.event.dto.request;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;

public record EventCreateRequest(
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
	public Event toEntity(EventHall eventHall) {
		return Event.builder()
			.title(title)
			.description(description)
			.runningTime(runningTime)
			.startDate(startDate)
			.endDate(endDate)
			.rating(rating)
			.genreType(genreType)
			.thumbnail(thumbnail)
			.eventHall(eventHall)
			.build();
	}
}
