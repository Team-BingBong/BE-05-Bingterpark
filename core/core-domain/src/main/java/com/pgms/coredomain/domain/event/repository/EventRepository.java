package com.pgms.coredomain.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	Boolean existsEventByTitle(String title);
}
