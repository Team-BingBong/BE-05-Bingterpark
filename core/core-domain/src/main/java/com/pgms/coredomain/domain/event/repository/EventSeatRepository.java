package com.pgms.coredomain.domain.event.repository;

import com.pgms.coredomain.domain.event.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {

	@Query("select es from EventSeat es join fetch es.eventSeatArea esa where es.eventTime.id = :eventTimeId")
	List<EventSeat> findAllWithAreaByEventTimeId(@Param("eventTimeId") Long eventTimeId);
}
