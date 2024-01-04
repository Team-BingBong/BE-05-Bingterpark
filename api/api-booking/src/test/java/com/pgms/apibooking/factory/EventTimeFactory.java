package com.pgms.apibooking.factory;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventTime;

public class EventTimeFactory {

	public static EventTime generate(Event event, LocalDateTime startedAt, LocalDateTime endedAt) {
		return EventTime.builder()
			.round(1)
			.startedAt(startedAt)
			.endedAt(endedAt)
			.event(event)
			.build();
	}
}
