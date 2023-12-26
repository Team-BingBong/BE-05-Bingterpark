package com.pgms.coredomain.domain.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.event.EventReview;

public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
	@Query("SELECT er FROM EventReview er WHERE er.event.id = :eventId")
	List<EventReview> findEventReviewsByEventId(@Param("eventId") Long eventId);

	@Query("SELECT ROUND(AVG(er.score), 1) FROM EventReview er WHERE er.event.id = :eventId")
	Double findAverageScoreByEvent(@Param("eventId") Long eventId);
}
