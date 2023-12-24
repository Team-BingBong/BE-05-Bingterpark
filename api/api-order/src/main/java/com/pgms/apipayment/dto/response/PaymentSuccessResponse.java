package com.pgms.apipayment.dto.response;

public record PaymentSuccessResponse(
	String paymentKey,
	String orderId,
	String orderName,
	String method,
	String totalAmount,
	String status,
	String requestedAt,
	String approvedAt,
	PaymentSuccessCardResponse card
) {
}
