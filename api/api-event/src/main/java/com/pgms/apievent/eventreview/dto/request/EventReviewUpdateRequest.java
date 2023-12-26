package com.pgms.apievent.eventreview.dto.request;

import jakarta.validation.constraints.Size;

public record EventReviewUpdateRequest(
	@Size(max = 1000, message = "공연 리뷰 내용은 최대 1,000자까지 입력 가능 합니다.")
	String content
) {
}
