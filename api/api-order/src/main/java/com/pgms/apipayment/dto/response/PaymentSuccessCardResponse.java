package com.pgms.apipayment.dto.response;

public record PaymentSuccessCardResponse(
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {
}
