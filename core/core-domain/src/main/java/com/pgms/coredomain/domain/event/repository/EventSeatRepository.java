package com.pgms.coredomain.domain.event.repository;

import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {

	@Query("SELECT es FROM EventSeat es JOIN FETCH es.eventSeatArea esa WHERE es.eventTime.id = :eventTimeId")
	List<EventSeat> findAllWithAreaByEventTimeId(@Param("eventTimeId") Long eventTimeId);

	@Query("SELECT es FROM EventSeat es JOIN FETCH es.eventSeatArea esa WHERE es.eventTime.id = :eventTimeId AND es.id IN :seatIds AND es.status = :status")
	List<EventSeat> findAllWithAreaByEventTimeIdAndSeatIdsAndStatus(Long eventTimeId, List<Long> seatIds, EventSeatStatus status);

	// TODO 특정 이벤트 회차의 등급별 남은 자리 갯수 쿼리
}
