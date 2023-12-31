package com.pgms.apievent.factory.event;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;

public class EventFactory {

	public static Event createEvent(EventHall eventHall) {
		return Event.builder()
			.title("공연 1")
			.description("공연1 입니다.")
			.runningTime(60)
			.startedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
			.endedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
			.rating("12세 이상")
			.genreType(GenreType.MUSICAL)
			.thumbnail("thumbnail.jpg")
			.bookingStartedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
			.bookingEndedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
			.eventHall(eventHall)
			.build();
	}
}
