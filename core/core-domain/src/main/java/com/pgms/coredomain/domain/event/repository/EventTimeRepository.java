package com.pgms.coredomain.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pgms.coredomain.domain.event.EventTime;

public interface EventTimeRepository extends JpaRepository<EventTime, Long> {
	@Query("SELECT COUNT(et) > 0 FROM EventTime et WHERE et.event.id = :eventId AND et.round = :round")
	boolean existsEventTimeForEventByRound(Long eventId, int round);
}
