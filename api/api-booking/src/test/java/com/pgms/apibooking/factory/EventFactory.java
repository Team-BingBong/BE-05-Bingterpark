package com.pgms.apibooking.factory;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;

public class EventFactory {

	public static Event generate(
		EventHall hall,
		LocalDateTime startedAt,
		LocalDateTime endedAt,
		LocalDateTime bookingStartedAt,
		LocalDateTime bookingEndedAt
	) {
		return Event.builder()
			.title("BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL")
			.description("BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL")
			.runningTime(120)
			.startedAt(startedAt)
			.endedAt(endedAt)
			.viewRating("15세 이상 관람가")
			.genreType(GenreType.CONCERT)
			.thumbnail("https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif")
			.bookingStartedAt(bookingStartedAt)
			.bookingEndedAt(bookingEndedAt)
			.eventHall(hall)
			.build();
	}
}
