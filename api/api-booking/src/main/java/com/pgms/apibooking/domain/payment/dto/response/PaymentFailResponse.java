package com.pgms.apibooking.domain.payment.dto.response;

public record PaymentFailResponse(
	String errorCode,
	String errorMessage,
	String orderId
) {
}
