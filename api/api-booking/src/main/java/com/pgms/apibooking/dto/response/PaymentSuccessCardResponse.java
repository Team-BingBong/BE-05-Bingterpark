package com.pgms.apibooking.dto.response;

public record PaymentSuccessCardResponse(
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {
}
