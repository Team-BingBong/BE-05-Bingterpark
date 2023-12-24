package com.pgms.coredomain.domain.event.repository;

import com.pgms.coredomain.domain.event.EventHall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventHallRepository extends JpaRepository<EventHall, Long> {
}
