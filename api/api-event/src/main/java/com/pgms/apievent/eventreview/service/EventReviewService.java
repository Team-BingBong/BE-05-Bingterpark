package com.pgms.apievent.eventreview.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.eventreview.dto.request.EventReviewCreateRequest;
import com.pgms.apievent.eventreview.dto.request.EventReviewUpdateRequest;
import com.pgms.apievent.eventreview.dto.response.EventReviewResponse;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventReview;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventReviewService {

	private final EventReviewRepository eventReviewRepository;
	private final EventRepository eventRepository;

	public EventReviewResponse createEventReview(Long eventId, EventReviewCreateRequest request) {
		Event event = eventRepository.findById(eventId).
			orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
		EventReview eventReview = eventReviewRepository.save(request.toEntity(event));

		Double averageScore = eventReviewRepository.findAverageScoreByEvent(event.getId());
		event.updateAverageScore(averageScore);

		return EventReviewResponse.of(eventReview);
	}

	public EventReviewResponse updateEventReview(Long reviewId, EventReviewUpdateRequest request) {
		EventReview eventReview = eventReviewRepository.findById(reviewId)
			.orElseThrow(() -> new EventException(EVENT_REVIEW_NOT_FOUND));
		eventReview.updateEventReview(request.content());
		return EventReviewResponse.of(eventReview);
	}

	@Transactional(readOnly = true)
	public EventReviewResponse getEventReviewById(Long reviewId) {
		EventReview eventReview = eventReviewRepository.findById(reviewId)
			.orElseThrow(() -> new EventException(EVENT_REVIEW_NOT_FOUND));
		return EventReviewResponse.of(eventReview);
	}

	@Transactional(readOnly = true)
	public List<EventReviewResponse> getEventReviewsForEventByEventId(Long eventId) {
		List<EventReview> eventReviews = eventReviewRepository.findEventReviewsByEventId(eventId);
		return eventReviews.stream()
			.map(EventReviewResponse::of)
			.toList();
	}

	public void deleteEventReviewById(Long reviewId) {
		EventReview eventReview = eventReviewRepository.findById(reviewId)
			.orElseThrow(() -> new EventException(EVENT_REVIEW_NOT_FOUND));
		eventReviewRepository.delete(eventReview);
	}
}
