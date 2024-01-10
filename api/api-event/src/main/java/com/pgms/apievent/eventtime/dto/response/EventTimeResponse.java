package com.pgms.apievent.eventtime.dto.response;

import com.pgms.coredomain.domain.event.EventTime;

import java.time.LocalDateTime;

public record EventTimeResponse(
	Long id,
	Long eventId,
	int round,
	LocalDateTime startedAt,
	LocalDateTime endedAt) {
	
	public static EventTimeResponse of(EventTime eventTime) {
		return new EventTimeResponse(
			eventTime.getId(),
			eventTime.getEvent().getId(),
			eventTime.getRound(),
			eventTime.getStartedAt(),
			eventTime.getEndedAt()
		);
	}
}
