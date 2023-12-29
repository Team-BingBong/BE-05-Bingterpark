package com.pgms.apibooking.dto.request;

public record PaymentCancelRequest(
	String paymentKey,
	String cancelReason
) {
}
