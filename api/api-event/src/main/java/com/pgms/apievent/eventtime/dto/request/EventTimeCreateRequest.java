package com.pgms.apievent.eventtime.dto.request;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventTime;

public record EventTimeCreateRequest(
	int round,
	LocalDateTime startTime,
	LocalDateTime endTime
) {
	public EventTime toEntity(Event event) {
		return new EventTime(
			round,
			startTime,
			endTime,
			event);
	}
}
