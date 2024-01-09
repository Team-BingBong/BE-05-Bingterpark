package com.pgms.apievent.eventtime.dto.request;

import java.time.LocalDateTime;

public record EventTimeUpdateRequest(LocalDateTime startTime, LocalDateTime endTime) {
}
