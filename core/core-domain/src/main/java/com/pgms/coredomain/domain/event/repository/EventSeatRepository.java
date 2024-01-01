package com.pgms.coredomain.domain.event.repository;

import com.pgms.coredomain.domain.event.EventSeat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {

	@Query("SELECT es FROM EventSeat es JOIN FETCH es.eventSeatArea esa WHERE es.eventTime.id = :timeId")
	List<EventSeat> findAllWithAreaByTimeId(@Param("timeId") Long timeId);

	@Query("SELECT es FROM EventSeat es JOIN FETCH es.eventSeatArea esa WHERE es.eventTime.id = :timeId AND es.id IN :seatIds")
	List<EventSeat> findAllWithAreaByTimeIdAndSeatIds(@Param("timeId") Long timeId, @Param("seatIds") List<Long> seatIds);

}
