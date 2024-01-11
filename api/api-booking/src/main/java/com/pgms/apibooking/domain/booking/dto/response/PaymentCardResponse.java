package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.coredomain.domain.booking.Payment;

public record PaymentCardResponse(
	String issuer,
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {

	public static PaymentCardResponse from(Payment payment) {
		return new PaymentCardResponse(
			payment.getCardIssuer().getName(),
			payment.getCardNumber(),
			payment.getInstallmentPlanMonths(),
			payment.isInterestFree()
		);
	}
}
