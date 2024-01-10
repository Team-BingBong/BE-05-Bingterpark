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
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventReviewService {

	private final EventReviewRepository eventReviewRepository;
	private final EventRepository eventRepository;
	private final MemberRepository memberRepository;

	public EventReviewResponse createEventReview(Long memberId, Long eventId, EventReviewCreateRequest request) {
		Member member = getMember(memberId);
		Event event = eventRepository.findById(eventId).
			orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
		EventReview eventReview = eventReviewRepository.save(request.toEntity(event, member));

		Double averageScore = eventReviewRepository.findAverageScoreByEvent(event.getId());
		event.updateAverageScore(averageScore);

		return EventReviewResponse.of(eventReview);
	}

	public EventReviewResponse updateEventReview(Long memberId, Long reviewId, EventReviewUpdateRequest request) {
		Member member = getMember(memberId);
		EventReview eventReview = getEventReview(reviewId);
		validateReviewer(eventReview, member);
		eventReview.updateEventReview(request.content());
		return EventReviewResponse.of(eventReview);
	}

	@Transactional(readOnly = true)
	public EventReviewResponse getEventReviewById(Long reviewId) {
		EventReview eventReview = getEventReview(reviewId);
		return EventReviewResponse.of(eventReview);
	}

	@Transactional(readOnly = true)
	public List<EventReviewResponse> getEventReviewsForEventByEventId(Long eventId) {
		List<EventReview> eventReviews = eventReviewRepository.findEventReviewsByEventId(eventId);
		return eventReviews.stream()
			.map(EventReviewResponse::of)
			.toList();
	}

	public void deleteEventReviewById(Long memberId, Long reviewId) {
		Member member = getMember(memberId);
		EventReview eventReview = getEventReview(reviewId);
		validateReviewer(eventReview, member);
		eventReviewRepository.delete(eventReview);
	}

	private EventReview getEventReview(Long reviewId) {
		return eventReviewRepository.findById(reviewId)
			.orElseThrow(() -> new EventException(EVENT_REVIEW_NOT_FOUND));
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.get();
	}

	private void validateReviewer(EventReview eventReview, Member member) {
		if (!eventReview.isSameReviewer(member)) {
			throw new EventException(REVIEWER_MISMATCH_EXCEPTION);
		}
	}
}
