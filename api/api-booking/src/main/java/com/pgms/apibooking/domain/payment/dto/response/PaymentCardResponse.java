package com.pgms.apibooking.domain.payment.dto.response;

public record PaymentCardResponse(
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {
}
