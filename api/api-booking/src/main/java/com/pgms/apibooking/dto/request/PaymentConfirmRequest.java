package com.pgms.apibooking.dto.request;

public record PaymentConfirmRequest(
	String paymentKey,
	String orderId, // 토스페이먼츠 요청을 위해 bookingId로 하면 안됨
	int amount
) {
}
