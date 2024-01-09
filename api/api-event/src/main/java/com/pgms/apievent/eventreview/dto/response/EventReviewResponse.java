package com.pgms.apievent.eventreview.dto.response;

import com.pgms.coredomain.domain.event.EventReview;

public record EventReviewResponse(
	Long id,
	int score,
	String content,
	Long eventId) {
	
	public static EventReviewResponse of(EventReview eventReview) {
		return new EventReviewResponse(
			eventReview.getId(),
			eventReview.getScore(),
			eventReview.getContent(),
			eventReview.getEvent().getId()
		);
	}
}
