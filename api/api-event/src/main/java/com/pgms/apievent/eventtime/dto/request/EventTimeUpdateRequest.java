package com.pgms.apievent.eventtime.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record EventTimeUpdateRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startedAt,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endedAt) {
}
