package com.pgms.apibooking.dto.response;

public record PaymentCancelDetailResponse(
	String cancelReason,
	int cancelAmount,
	String canceledAt
) {
}
