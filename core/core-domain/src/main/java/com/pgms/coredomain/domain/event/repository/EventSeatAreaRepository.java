package com.pgms.coredomain.domain.event.repository;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventSeatArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventSeatAreaRepository extends JpaRepository<EventSeatArea, Long> {
    List<EventSeatArea> findEventSeatAreasByEvent(Event event);
}
