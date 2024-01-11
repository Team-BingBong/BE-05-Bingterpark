package com.pgms.apibooking.domain.payment.dto.response;

public record PaymentCardResponse(
	String issuerCode,
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {
}
