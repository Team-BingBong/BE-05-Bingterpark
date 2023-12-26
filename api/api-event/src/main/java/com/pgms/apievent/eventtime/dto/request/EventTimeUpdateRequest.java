package com.pgms.apievent.eventtime.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record EventTimeUpdateRequest(
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime startTime,

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime endTime
) {
}
