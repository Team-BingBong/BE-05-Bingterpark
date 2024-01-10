package com.pgms.apievent.eventtime.dto.request;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventTime;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record EventTimeCreateRequest(

	@Positive(message = "회차는 0보다 큰 값 이어야 합니다.")
	int round,
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime startedAt,
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime endedAt) {
	
	public EventTime toEntity(Event event) {
		return new EventTime(
			round,
			startedAt,
			endedAt,
			event);
	}
}
