package com.pgms.apibooking.dto.response;

public record PaymentFailResponse(
	String errorCode,
	String errorMessage,
	String orderId
) {
}
