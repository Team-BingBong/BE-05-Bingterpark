package com.pgms.apibooking.domain.payment.dto.response;

public record PaymentCancelDetailResponse(
	String cancelReason,
	Integer cancelAmount,
	String canceledAt
) {
}
