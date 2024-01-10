package com.pgms.apievent.factory.eventreview;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventReview;
import com.pgms.coredomain.domain.member.Member;

public class EventReviewFactory {

	public static EventReview createEventReview(Event event, Member member) {
		return new EventReview(
			4,
			"공연 후기입니다.",
			event,
			member
		);
	}
}
