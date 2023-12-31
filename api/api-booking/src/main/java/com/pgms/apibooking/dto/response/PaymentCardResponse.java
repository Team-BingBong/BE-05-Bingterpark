package com.pgms.apibooking.dto.response;

public record PaymentCardResponse(
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {
}
