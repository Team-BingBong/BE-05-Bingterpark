package com.pgms.apievent.eventreview.dto.request;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventReview;
import com.pgms.coredomain.domain.member.Member;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record EventReviewCreateRequest(
	@PositiveOrZero(message = "리뷰 점수는 0이상의 값 이어야 합니다.")
	int score,

	@Size(max = 1000, message = "공연 리뷰 내용은 최대 1,000자까지 입력 가능 합니다.")
	String content) {

	public EventReview toEntity(Event event, Member member) {
		return new EventReview(
			score,
			content,
			event,
			member
		);
	}
}
