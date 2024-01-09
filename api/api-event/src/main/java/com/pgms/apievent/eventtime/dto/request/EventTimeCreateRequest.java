package com.pgms.apievent.eventtime.dto.request;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventTime;

import jakarta.validation.constraints.Positive;

public record EventTimeCreateRequest(

	@Positive(message = "회차는 0보다 큰 값 이어야 합니다.")
	int round,
	LocalDateTime startTime,
	LocalDateTime endTime) {
	
	public EventTime toEntity(Event event) {
		return new EventTime(
			round,
			startTime,
			endTime,
			event);
	}
}
