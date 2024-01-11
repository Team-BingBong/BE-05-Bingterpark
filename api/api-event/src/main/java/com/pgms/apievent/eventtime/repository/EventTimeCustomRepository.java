package com.pgms.apievent.eventtime.repository;

import java.time.LocalDateTime;

public interface EventTimeCustomRepository {
	Boolean isAlreadyExistEventPlayTime(LocalDateTime startedAt, LocalDateTime endedAt, Long eventId);
}
