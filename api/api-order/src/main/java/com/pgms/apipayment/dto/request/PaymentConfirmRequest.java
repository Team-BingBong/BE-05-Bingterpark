package com.pgms.apipayment.dto.request;

public record PaymentConfirmRequest(
	String paymentKey, String orderId, int amount
) {
}
