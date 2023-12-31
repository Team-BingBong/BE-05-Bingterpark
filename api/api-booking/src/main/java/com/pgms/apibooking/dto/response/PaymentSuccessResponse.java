package com.pgms.apibooking.dto.response;

public record PaymentSuccessResponse(
	String paymentKey,
	String orderId,
	String orderName,	// 토스페이먼츠에서 제공해주는 값이라 booking으로 변경x
	String method,
	int totalAmount,
	String status,
	String requestedAt,
	String approvedAt,
	PaymentCardResponse card,
	PaymentVirtualResponse virtualAccount
) {
}

