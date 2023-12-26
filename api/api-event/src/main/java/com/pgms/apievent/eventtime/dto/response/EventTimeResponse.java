package com.pgms.apievent.eventtime.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.EventTime;

public record EventTimeResponse(
	Long id,
	Long eventId,
	int round,
	LocalDateTime startTime,
	LocalDateTime endTime
) {
	public static EventTimeResponse of(EventTime eventTime) {
		return new EventTimeResponse(
			eventTime.getId(),
			eventTime.getEvent().getId(),
			eventTime.getRound(),
			eventTime.getStartTime(),
			eventTime.getEndTime()
		);
	}
}
